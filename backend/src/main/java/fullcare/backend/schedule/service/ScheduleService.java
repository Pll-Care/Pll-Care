package fullcare.backend.schedule.service;

import fullcare.backend.global.State;
import fullcare.backend.global.errorcode.MemberErrorCode;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.errorcode.ScheduleErrorCode;
import fullcare.backend.global.exceptionhandling.exception.CompletedProjectException;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.global.exceptionhandling.exception.ScheduleCategoryMisMatchException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.project.service.ProjectService;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import fullcare.backend.schedule.DateCategory;
import fullcare.backend.schedule.ScheduleCategory;
import fullcare.backend.schedule.ScheduleCondition;
import fullcare.backend.schedule.domain.Meeting;
import fullcare.backend.schedule.domain.Milestone;
import fullcare.backend.schedule.domain.Schedule;
import fullcare.backend.schedule.dto.*;
import fullcare.backend.schedule.dto.request.ScheduleStateUpdateRequest;
import fullcare.backend.schedule.dto.request.ScheduleUpdateRequest;
import fullcare.backend.schedule.dto.response.*;
import fullcare.backend.schedule.repository.ScheduleRepository;
import fullcare.backend.schedule.repository.ScheduleRepositoryCustom;
import fullcare.backend.schedulemember.domain.ScheduleMember;
import fullcare.backend.schedulemember.repository.ScheduleMemberRepository;
import fullcare.backend.util.CustomPageImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static fullcare.backend.global.errorcode.ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND;
import static fullcare.backend.global.errorcode.ScheduleErrorCode.INVALID_DELETE;
import static fullcare.backend.global.errorcode.ScheduleErrorCode.INVALID_MODIFY;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final ScheduleRepositoryCustom scheduleRepositoryCustom;
    private final ProjectService projectService;


    // * VALIDATE
    // * 작성자 검증
    public boolean validateAuthor(Long projectId, Long scheduleId, Long authorId) {
        ProjectMember projectMember = projectMemberRepository.findPMWithProjectByProjectIdAndMemberId(projectId, authorId).orElseThrow(() -> new EntityNotFoundException(PROJECT_MEMBER_NOT_FOUND));
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        if (schedule.getAuthor().getId() == projectMember.getId()) {
            return true;
        }
        return false;
    }

    // * 삭제 권한 검증
    public boolean validateDelete(Long scheduleId, Long projectId, Long memberId, boolean readOnly) {
        try {
            ProjectMember projectMember = projectService.isProjectAvailable(projectId, memberId, readOnly);
            return !(!validateAuthor(projectId, scheduleId, memberId) && !projectMember.isLeader());
        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(INVALID_DELETE);
        }
    }

    // * READ
    // * 프로젝트 전체 MILESTONE 일정
    // * 프로젝트 기한이 대략 3달 이상일 경우 MONTH, 이하면 WEEK 로 분리
    @Transactional(readOnly = true)
    public OverallScheduleResponse findScheduleList(Long projectId, Long memberId) {
        Project project = projectService.isProjectAvailable(projectId, memberId, true).getProject();
        LocalDate startDate = project.getStartDate();
        LocalDate endDate = project.getEndDate();
        long diff = ChronoUnit.WEEKS.between(startDate, endDate);
        DateCategory dateCategory = diff > 13 ? DateCategory.MONTH : DateCategory.WEEK;
        List<Schedule> milestoneList = scheduleRepository.findMileStoneByProjectId(projectId);
        List<ScheduleDto> scheduleListResponseList = toOverallResponse(startDate, milestoneList, dateCategory);
        OverallScheduleResponse response = new OverallScheduleResponse(startDate, endDate, dateCategory, scheduleListResponseList);
        return response;
    }

    @Transactional(readOnly = true)
    public ScheduleCalenderMonthResponse findScheduleCalenderList(Long projectId, Long memberId) {
        projectService.isProjectAvailable(projectId, memberId, true);
        List<Schedule> scheduleList = scheduleRepository.findByProjectId(projectId);
        ScheduleCalenderMonthResponse scheduleMonthResponse = toCalenderResponse(scheduleList);
        scheduleMonthResponse.getMeetings().sort(Comparator.comparing(MeetingDto::getStartDate));// * 시작 날짜 기준 내림차순 정렬
        scheduleMonthResponse.getMilestones().sort(Comparator.comparing(MilestoneDto::getStartDate));// * 시작 날짜 기준 내림차순 정렬
        return scheduleMonthResponse;
    }

    @Transactional
    public List<ScheduleDailyResponse> findDailySchedule(Long projectId, Long memberId) {
        projectService.isProjectAvailable(projectId, memberId, true);
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 23, 59, 59);
        List<Schedule> scheduleList = scheduleRepository.findDaily(projectId, startDate, endDate);
        List<ScheduleDailyResponse> scheduleDailyResponse = new ArrayList<>();
        toDailyResponse(scheduleList, scheduleDailyResponse);// * 미팅, 마일스톤에 맞게 일정 생성 후 응답에 넣기
        return scheduleDailyResponse;
    }

    // * 사용자별 and (미팅 or 마일스톤 or 지난일정) 필터 조회
    @Transactional
    public CustomPageImpl<ScheduleSearchResponse> searchScheduleList(Pageable pageable, Member member, ScheduleCondition scheduleCondition) {
        projectService.isProjectAvailable(scheduleCondition.getProjectId(), member.getId(), true);
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
        List<Schedule> scheduleList = scheduleRepositoryCustom.search(scheduleCondition, scheduleCondition.getProjectId());// * 모든 일정을 가져와서 pagination 생성
        List<ScheduleSearchResponse> content = toSearchResponse(scheduleList, findMember, scheduleCondition.isPrevious());//*  미팅, 마일스톤에 맞게 일정 생성 후 응답에 넣기
        List<ScheduleSearchResponse> response = createPagination(pageable, content); // * 페이지네이션 설정
        return new CustomPageImpl<>(response, pageable, content.size());
    }

    @Transactional
    public ScheduleDetailResponse findSchedule(Long scheduleId, Long projectId, Long memberId) {
        Project project = projectService.isProjectAvailable(projectId, memberId, true).getProject();
        Schedule schedule = scheduleRepository.findJoinSMById(scheduleId).orElseThrow(() -> new EntityNotFoundException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        List<ProjectMember> projectMembers = project.getProjectMembers();
        List<ScheduleMember> scheduleMembers = schedule.getScheduleMembers();
        ScheduleDetailResponse scheduleDetailResponse = ScheduleDetailResponse.builder()
                .projectId(project.getId())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .deleteAuthorization(validateDelete(scheduleId, project.getId(), memberId, true))
                .build();

        for (ProjectMember projectMember : projectMembers) {
            DetailMemberDto detailMemberDto = DetailMemberDto.builder()
                    .id(projectMember.getMember().getId())
                    .name(projectMember.getMember().getName())
                    .build();
            for (ScheduleMember scheduleMember : scheduleMembers) {
                if (projectMember.getMember() == scheduleMember.getProjectMember().getMember()) {
                    detailMemberDto.setIn(true);
                }
                if (projectMember.getMember() == scheduleMember.getProjectMember().getMember() && projectMember.getMember().getId() == memberId) { // * 로그인한 사용자 최근 확인한 시간 갱신
                    scheduleMember.updateRecentView(LocalDateTime.now());
                }
            }
            scheduleDetailResponse.addMember(detailMemberDto);
        }

        if (schedule instanceof Meeting) {
            Meeting meeting = (Meeting) schedule;
            scheduleDetailResponse.setScheduleCategory(ScheduleCategory.MEETING);
            scheduleDetailResponse.setAddress(meeting.getAddress());
        } else {
            scheduleDetailResponse.setScheduleCategory(ScheduleCategory.MILESTONE);
        }
        return scheduleDetailResponse;

    }

    // * UPDATE
    public boolean updateSchedule(ScheduleUpdateRequest scheduleUpdateRequest, Long scheduleId, Long memberId) {
        try {
            projectService.isProjectAvailable(scheduleUpdateRequest.getProjectId(), memberId, false);
        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(INVALID_MODIFY);
        }
        Schedule schedule = scheduleRepository.findJoinSMById(scheduleId).orElseThrow(() -> new EntityNotFoundException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        if ((schedule instanceof Meeting && scheduleUpdateRequest.getCategory().equals(ScheduleCategory.MILESTONE)) || (schedule instanceof Milestone && scheduleUpdateRequest.getCategory().equals(ScheduleCategory.MEETING))) {
            throw new ScheduleCategoryMisMatchException(ScheduleErrorCode.INVALID_CATEGORY_MODIFY);
        }
        if (schedule.getState().equals(State.COMPLETE)) {
            throw new ScheduleCategoryMisMatchException(ScheduleErrorCode.SCHEDULE_COMPLETED);
        }
        Project project = projectRepository.findById(scheduleUpdateRequest.getProjectId()).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
        LocalDateTime startDate = project.getStartDate().atStartOfDay();
        LocalDateTime endDate = project.getEndDate().atStartOfDay();
        Schedule.validDate(startDate, endDate, scheduleUpdateRequest.getStartDate(), scheduleUpdateRequest.getEndDate());
        List<ProjectMemberRoleType> projectMemberRoleTypes = new ArrayList<>();
        projectMemberRoleTypes.add(ProjectMemberRoleType.리더);
        projectMemberRoleTypes.add(ProjectMemberRoleType.팀원);
        List<ProjectMember> pmList = projectMemberRepository.findProjectMemberWithMemberByProjectIdAndProjectMemberRole(scheduleUpdateRequest.getProjectId(), projectMemberRoleTypes);// * 프로젝트에 있는 멤버 리스트
        schedule.update(
                scheduleUpdateRequest.getState(),
                scheduleUpdateRequest.getTitle(),
                scheduleUpdateRequest.getContent(),
                scheduleUpdateRequest.getStartDate(),
                scheduleUpdateRequest.getEndDate(),
                LocalDateTime.now()
        );
        List<ProjectMember> updateMemberList = projectMemberRepository.findByProjectIdAndMemberIds(scheduleUpdateRequest.getProjectId(), scheduleUpdateRequest.getMemberIds());// * 새로 업데이트 되는 멤버 리스트
        if (!pmList.containsAll(updateMemberList)) {// * 갱신된 사람이 프로젝트에 속한 사람인지 확인
            throw new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND);
        }
        schedule.getScheduleMembers().clear();
        for (ProjectMember projectMember : updateMemberList) {// * 일정에 속한 사람 갱신
            schedule.addMember(projectMember);
        }

        if (scheduleUpdateRequest.getCategory().equals(ScheduleCategory.MEETING)) { // * Meeting 주소 추가
            Meeting meeting = (Meeting) schedule;
            meeting.updateAddress(scheduleUpdateRequest.getAddress());
        } else { // Milestone
        }
        return true;
    }

    public void updateState(ScheduleStateUpdateRequest scheduleStateUpdateRequest, Long scheduleId, Long memberId) {
        try {
            projectService.isProjectAvailable(scheduleStateUpdateRequest.getProjectId(), memberId, false);
            Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
            LocalDateTime now = LocalDateTime.now();
            schedule.updateState(now, scheduleStateUpdateRequest.getState());
            scheduleMemberRepository.updateRecentView(now, schedule.getId());// * 상태 바꿀 때도 schedulemember recentview 바꿔야함
        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(INVALID_MODIFY);
        }
    }

    // * DELETE
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        scheduleRepository.delete(schedule);
    }


    // * INTERNAL METHOD
    private ScheduleCalenderMonthResponse toCalenderResponse(List<Schedule> scheduleList) {
        ScheduleCalenderMonthResponse scheduleMonthResponse = new ScheduleCalenderMonthResponse();
        for (Schedule schedule : scheduleList) {
            if (schedule instanceof Meeting) {
                Meeting meeting = (Meeting) schedule;
                MeetingDto meetingDto = MeetingDto.builder()
                        .scheduleId(schedule.getId())
                        .title(schedule.getTitle())
                        .content(meeting.getContent())
                        .startDate(meeting.getStartDate())
                        .endDate(meeting.getEndDate())
                        .address(meeting.getAddress()).build();
                List<ScheduleMember> scheduleMembers = schedule.getScheduleMembers();
                scheduleMembers.forEach(sm -> {
                    meetingDto.addMember(sm.getProjectMember().getMember());
                });
                scheduleMonthResponse.addMeeting(meetingDto);
            } else {
                Milestone milestone = (Milestone) schedule;
                MilestoneDto milestoneDto = MilestoneDto.builder()
                        .scheduleId(milestone.getId())
                        .title(milestone.getTitle())
                        .content(milestone.getContent())
                        .startDate(milestone.getStartDate())
                        .endDate(milestone.getEndDate())
                        .build();
                List<ScheduleMember> scheduleMembers = milestone.getScheduleMembers();
                scheduleMembers.forEach(sm -> {
                    milestoneDto.addMember(sm.getProjectMember().getMember());
                });
                scheduleMonthResponse.addMilestone(milestoneDto);
            }
        }
        return scheduleMonthResponse;
    }

    private List<ScheduleDto> toOverallResponse(LocalDate startDate, List<Schedule> scheduleList, DateCategory dateCategory) {
        List<ScheduleDto> scheduleListResponseList = new ArrayList<>();
        ScheduleDto scheduleDto = null;
        if (!scheduleList.isEmpty()) {
            Long startMonth = scheduleList.get(0).getStartDate().getMonth().getLong(ChronoField.MONTH_OF_YEAR);
            Long order;
            for (Schedule s : scheduleList) {
                if (dateCategory.equals(DateCategory.MONTH)) {// * 월끼리 순서 분리
                    order = s.getStartDate().getMonth().getLong(ChronoField.MONTH_OF_YEAR) - startMonth + 1;
                } else { // * 2주씩 순서 분리
                    order = ChronoUnit.WEEKS.between(startDate, s.getStartDate()) / 2 + 1;
                }
                scheduleDto = ScheduleDto.builder()
                        .id(s.getId())
                        .title(s.getTitle())
                        .startDate(s.getStartDate())
                        .endDate(s.getEndDate())
                        .order(order)
                        .build();
                scheduleListResponseList.add(scheduleDto);
            }
        }
        return scheduleListResponseList;
    }

    private void toDailyResponse(List<Schedule> scheduleList, List<ScheduleDailyResponse> scheduleDailyResponse) {
        LocalDateTime now = LocalDateTime.now();
        for (Schedule schedule : scheduleList) {
            setOngoing(now, schedule);
            ScheduleDailyResponse scheduleResponse = ScheduleDailyResponse.builder()
                    .scheduleId(schedule.getId())
                    .title(schedule.getTitle())
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getEndDate())
                    .build();

            List<ScheduleMember> scheduleMembers = schedule.getScheduleMembers();
            scheduleMembers.forEach(sm -> {
                scheduleResponse.addMember(sm.getProjectMember().getMember());
            });
            if (schedule instanceof Meeting) {
                scheduleResponse.setAddress(((Meeting) schedule).getAddress());
                scheduleResponse.setScheduleCategory(ScheduleCategory.MEETING);
            } else {
                scheduleResponse.setScheduleCategory(ScheduleCategory.MILESTONE);
            }
            scheduleDailyResponse.add(scheduleResponse);

        }
    }


    private List<ScheduleSearchResponse> toSearchResponse(List<Schedule> scheduleList, Member member, boolean previous) {
        List<ScheduleSearchResponse> scheduleSearchResponse = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Schedule schedule : scheduleList) {
            setOngoing(now, schedule);
            ScheduleSearchResponse scheduleResponse = ScheduleSearchResponse.builder()
                    .scheduleId(schedule.getId())
                    .title(schedule.getTitle())
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getEndDate())
                    .state(schedule.getState())
                    .modifyDate(schedule.getModifiedDate().toLocalDate())
                    .build();
            checkModify(member, schedule, scheduleResponse);
            if (schedule instanceof Meeting) {
                scheduleResponse.setScheduleCategory(ScheduleCategory.MEETING);
            } else {
                scheduleResponse.setScheduleCategory(ScheduleCategory.MILESTONE);
            }

            boolean eval = schedule.getMidtermEvaluations().stream().anyMatch(me -> me.getVoter().getId() == member.getId());//? 해당 일정에 평가한 적이 있는지 확인
            if (previous) { // * 평가된 일정만 찾아 넣기
                if (eval && schedule.getState().equals(State.COMPLETE)) {
                    scheduleSearchResponse.add(scheduleResponse);
                }
            } else { // * 평가한적 없는 일정을 찾아 평가가 필요하도록 값 설정
                if (!eval && schedule.getState().equals(State.COMPLETE)) { //? 평가한 적이 없는 완료된 일정을 고름
                    if (schedule.getScheduleMembers().stream().anyMatch(sm -> sm.getProjectMember().getMember().getId() == member.getId())) {//? 사용자가 일정에 들어갔는지 확인
                        scheduleResponse.setEvaluationRequired(true);
                    }
                }
                // * 평가가 필요하거나 완료되지 않은 일정만 찾아 넣기
                if (scheduleResponse.getEvaluationRequired() || scheduleResponse.getState().equals(State.TBD) || scheduleResponse.getState().equals(State.ONGOING))
                    scheduleSearchResponse.add(scheduleResponse);
            }
        }
        return scheduleSearchResponse;
    }

    // * 일정이 수정됐는지 확인한 사용자
    private void checkModify(Member member, Schedule schedule, ScheduleSearchResponse scheduleResponse) {
        List<ScheduleMember> scheduleMembers = schedule.getScheduleMembers();
        scheduleMembers.forEach(sm -> {
            scheduleResponse.addMember(sm.getProjectMember().getMember());
            if (sm.getProjectMember().getMember() == member && sm.getRecentView().isBefore(schedule.getModifiedDate())) { // * 수정된걸 확인 못함
                scheduleResponse.updateCheck(false);
            } else if (sm.getProjectMember().getMember() == member) {// * 수정된걸 확인함
                scheduleResponse.updateCheck(true);
            }
        });
    }


    // * 시작 시간이 지나면 일정 진행중으로 변경, 사용자가 일정을 확인한 시간 갱신
    private static void setOngoing(LocalDateTime now, Schedule schedule) {
        if (now.isAfter(schedule.getStartDate()) && schedule.getState().equals(State.TBD)) {
            schedule.updateState(now, State.ONGOING);
            for (ScheduleMember scheduleMember : schedule.getScheduleMembers()) {
                scheduleMember.updateRecentView(now);
            }
        }
    }

    // * 페이지네이션 설정
    private static List<ScheduleSearchResponse> createPagination(Pageable pageable, List<ScheduleSearchResponse> newResponse) {
        int pageNumber = pageable.getPageNumber();
        Long last = null;
        Long offset = pageable.getOffset();
        if ((pageNumber + 1) * pageable.getPageSize() > newResponse.size()) {
            last = Long.valueOf(newResponse.size());
        } else {
            last = pageable.getOffset() + pageable.getPageSize();
        }
        newResponse.sort(Comparator.comparing(ScheduleSearchResponse::getStartDate));// * 날짜 기준 내림차순 정렬
        List<ScheduleSearchResponse> subList = newResponse.subList(offset.intValue(), last.intValue());
        return subList;
    }
}
