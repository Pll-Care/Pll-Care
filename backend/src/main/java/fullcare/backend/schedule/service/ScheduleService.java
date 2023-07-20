package fullcare.backend.schedule.service;

import fullcare.backend.evaluation.repository.MidtermEvaluationRepository;
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
import fullcare.backend.schedule.dto.request.ScheduleMonthRequest;
import fullcare.backend.schedule.dto.request.ScheduleStateUpdateRequest;
import fullcare.backend.schedule.dto.request.ScheduleUpdateRequest;
import fullcare.backend.schedule.dto.response.*;
import fullcare.backend.schedule.repository.ScheduleRepository;
import fullcare.backend.schedule.repository.ScheduleRepositoryCustom;
import fullcare.backend.schedulemember.domain.ScheduleMember;
import fullcare.backend.schedulemember.repository.ScheduleMemberRepository;
import fullcare.backend.util.CustomPageImpl;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    private final EntityManager em;
    private final ScheduleRepositoryCustom scheduleRepositoryCustom;
    private final MidtermEvaluationRepository midtermEvaluationRepository;
//    @Transactional(readOnly = true)
//    public List<ProjectMemberListResponse> findProjectMembers(Long projectId){
////        Project project = projectRepository.findById(projectId).orElseThrow();
////        List<ScheduleServingResponse> response = project.getProjectMembers().stream().map(pms -> ScheduleServingResponse.builder()
////                .id(pms.getMember().getId())
////                .name(pms.getMember().getName()).build()
////        ).collect(Collectors.toList());
//
//        return getProjectMemberListResponses(projectId, projectMemberRepository);
//    }

    private static List<ScheduleSearchResponse> pageResponse(Pageable pageable, List<ScheduleSearchResponse> newResponse) {
        int pageNumber = pageable.getPageNumber();
        Long last = null;
        Long offset = pageable.getOffset();


        if ((pageNumber + 1) * pageable.getPageSize() > newResponse.size()) {
            last = Long.valueOf(newResponse.size());
        } else {
            last = pageable.getOffset() + pageable.getPageSize();
        }
        newResponse.sort(Comparator.comparing(ScheduleSearchResponse::getStartDate));// 날짜 기준 내림차순 정렬
        List<ScheduleSearchResponse> subList = newResponse.subList(offset.intValue(), last.intValue());
        return subList;
    }

    public boolean validateAuthor(Long projectId, Long scheduleId, Long authorId) {
        ProjectMember projectMember = projectMemberRepository.findPMWithProjectByProjectIdAndMemberId(projectId, authorId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        if (schedule.getMember().getId() == projectMember.getMember().getId()) {
            return true;
        }
        return false;
    }

    public boolean validateDelete(Long scheduleId, Long projectId, Long memberId, ProjectMember projectMember) {
        return !(!validateAuthor(projectId, scheduleId, memberId) && !projectMember.isLeader());
    }

    public boolean updateSchedule(ScheduleUpdateRequest scheduleUpdateRequest, Long scheduleId) {// 멤버 로그인 사용자 검증 수정
        Schedule schedule = scheduleRepository.findJoinSMById(scheduleId).orElseThrow(() -> new EntityNotFoundException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        if ((schedule instanceof Meeting && scheduleUpdateRequest.getCategory().equals(ScheduleCategory.MILESTONE)) || (schedule instanceof Milestone && scheduleUpdateRequest.getCategory().equals(ScheduleCategory.MEETING))) {
            throw new ScheduleCategoryMisMatchException(ScheduleErrorCode.CATEGORY_NOT_MODIFY);
        }
        if (schedule.getState().equals(State.COMPLETE)) {
            throw new ScheduleCategoryMisMatchException(ScheduleErrorCode.SCHEDULE_COMPLETED);
        }
        Project project = projectRepository.findById(scheduleUpdateRequest.getProjectId()).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
        LocalDateTime startDate = project.getStartDate().atStartOfDay();
        LocalDateTime endDate = project.getEndDate().atStartOfDay();
        Schedule.validDate(startDate, endDate, scheduleUpdateRequest.getStartDate(), scheduleUpdateRequest.getEndDate());
        if (project.isCompleted()) {
            throw new CompletedProjectException(ScheduleErrorCode.PC_SCHEDULE_NOT_PATCH);
        }
        List<ProjectMember> pmList = projectMemberRepository.findByProjectIdAndProjectMemberRole(scheduleUpdateRequest.getProjectId(), ProjectMemberRoleType.미정);

        List<Member> members = pmList.stream().map(pm -> pm.getMember()).collect(Collectors.toList());// 프로젝트에 있는 멤버 리스트
        schedule.update(
                scheduleUpdateRequest.getState(),
                scheduleUpdateRequest.getTitle(),
                scheduleUpdateRequest.getContent(),
                scheduleUpdateRequest.getStartDate(),
                scheduleUpdateRequest.getEndDate(),
                LocalDateTime.now()
        );

        List<Member> updateMemberList = memberRepository.findByIds(scheduleUpdateRequest.getMemberIds()); // 새로 업데이트 되는 멤버 리스트
        if (!members.containsAll(updateMemberList)) {// 프로젝트에 속한 사람인지 확인
            return false;
        }


        schedule.getScheduleMembers().clear(); // 어떤 사람이 들어오고, 나가고, 그대로 있는지를 파악을 해야함,
        for (Member member : updateMemberList) {
            schedule.addMember(member);
        }

        if (scheduleUpdateRequest.getCategory().equals(ScheduleCategory.MEETING)) { // Meeting
            Meeting meeting = (Meeting) schedule;
            meeting.updateAddress(scheduleUpdateRequest.getAddress());
        } else { // Milestone
        }
        return true;
    }

    public void deleteSchedule(Long scheduleId, Long projectId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
        if (project.isCompleted()) {
            throw new CompletedProjectException(ScheduleErrorCode.PC_SCHEDULE_NOT_PATCH);
        }
        scheduleRepository.delete(schedule);
    }

    @Transactional(readOnly = true)
    public CustomResponseDto findScheduleList(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
        LocalDate startDate = project.getStartDate();
        LocalDate endDate = project.getEndDate();
        long diff = ChronoUnit.WEEKS.between(startDate, endDate);
        DateCategory dateCategory;
        if (diff > 13) {
            dateCategory = DateCategory.MONTH;
        } else {
            dateCategory = DateCategory.WEEK;
        }

//        System.out.println("between = " + diff);

        //schedule로 통합 필요
//        List<Schedule> meetingList = scheduleRepository.findMeetingByProjectId(projectId); // dtype을 구분 짓기 위해 각 메소드를 만듬
//        List<ScheduleListResponse> scheduleListResponseList = toListResponse(meetingList, ScheduleCategory.미팅);

        List<Schedule> milestoneList = scheduleRepository.findMileStoneByProjectId(projectId);
        List<ScheduleListResponse> scheduleListResponseList = toListResponse(startDate, milestoneList, dateCategory);
        /////////////////
        //scheduleListResponseList.addAll(response2);
        CustomResponseDto response =
                diff > 13 ? new CustomResponseDto(startDate, endDate, DateCategory.MONTH, scheduleListResponseList)
                        : new CustomResponseDto(startDate, endDate, DateCategory.WEEK, scheduleListResponseList);
        //scheduleListResponseList.sort(Comparator.comparing(ScheduleListResponse::getStartDate));// 날짜 기준 내림차순 정렬
        return response;
    }

    @Transactional(readOnly = true)
    public ScheduleCalenderMonthResponse findScheduleCalenderList(Long projectId) { // 1일부터 31일까지 일정
//        LocalDateTime findDate = LocalDateTime.of(year, month, 1, 0, 0);
//        LocalDate localDate = findDate.toLocalDate();
//        int lastDay = findDate.toLocalDate().withDayOfMonth(localDate.lengthOfMonth()).getDayOfMonth();
//        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0,0);
//        LocalDateTime endDate = LocalDateTime.of(year, month,lastDay,23,59,59  );
//        List<Schedule> scheduleList = scheduleRepository.findByStartDateBetweenOrEndDateBetween( startDate, endDate,startDate, endDate  );

        List<Schedule> scheduleList = scheduleRepository.findByProjectId(projectId);
        ScheduleCalenderMonthResponse scheduleMonthResponse = toScheduleMonthResponse(scheduleList);
        scheduleMonthResponse.getMeetings().sort(Comparator.comparing(MeetingDto::getStartDate));// 날짜 기준 내림차순 정렬
        scheduleMonthResponse.getMilestones().sort(Comparator.comparing(MilestoneDto::getStartDate));// 날짜 기준 내림차순 정렬
        return scheduleMonthResponse;
    }

    @Transactional
    public CustomPageImpl<ScheduleMonthResponse> findScheduleMonthList(Pageable pageable, int year, int month) { // 1일부터 31일까지 일정
        LocalDateTime findDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDate localDate = findDate.toLocalDate();
        int lastDay = findDate.toLocalDate().withDayOfMonth(localDate.lengthOfMonth()).getDayOfMonth();
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, month, lastDay, 23, 59, 59);

        Page<Schedule> pageSchedule = scheduleRepository.findMonthByStartDateBetweenOrEndDateBetween(pageable, startDate, endDate, startDate, endDate);

        List<ScheduleMonthResponse> scheduleMonthResponse = new ArrayList<>();
        addResponse(pageSchedule, scheduleMonthResponse);// 미팅, 마일스톤에 맞게 일정 생성 후 응답에 넣기
        return new CustomPageImpl<>(scheduleMonthResponse, pageable, pageSchedule.getTotalElements());
    }

    @Transactional
    public CustomPageImpl<ScheduleSearchResponse> searchScheduleList(Pageable pageable, Member member, ScheduleCondition scheduleCondition) { // 1일부터 31일까지 일정
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
        List<Schedule> scheduleList = scheduleRepositoryCustom.search(scheduleCondition, scheduleCondition.getProjectId());//? 모든 일정을 가져와서 검증

        List<ScheduleSearchResponse> ScheduleSearchResponse = new ArrayList<>();
        List<ScheduleSearchResponse> content = createPageResponse(scheduleList, ScheduleSearchResponse, findMember, pageable);// 미팅, 마일스톤에 맞게 일정 생성 후 응답에 넣기
        List<ScheduleSearchResponse> response = pageResponse(pageable, content);
        return new CustomPageImpl<>(response, pageable, content.size());
    }

    //    @Transactional(readOnly = true)
//    public Page<ScheduleMonthResponse> findScheduleMemberMonthList(Pageable pageable, ScheduleMonthListRequest scheduleMonthListRequest) {
//        LocalDate localDate = LocalDateTime.now().toLocalDate();
//        int lastDay = LocalDateTime.now().toLocalDate().withDayOfMonth(localDate.lengthOfMonth()).getDayOfMonth();
//        LocalDateTime startDate = LocalDateTime.of(scheduleMonthListRequest.getYear(),scheduleMonthListRequest.getMonth(), 1, 0, 0,0);
//        LocalDateTime endDate = LocalDateTime.of(scheduleMonthListRequest.getYear(), scheduleMonthListRequest.getMonth(),lastDay,23,59,59  );
//        Page<Schedule> pageSchedule = scheduleRepository.findMonthByStartDateBetweenOrEndDateBetween(pageable, startDate, endDate, startDate, endDate);
//        return null;
//    }
    public void updateState(ScheduleStateUpdateRequest scheduleStateUpdateRequest, Long scheduleId) { // 상태 바꿀 때도 schedulemember recentview 바꿔야함
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        Project project = projectRepository.findById(scheduleStateUpdateRequest.getProjectId()).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
        if (project.isCompleted()) {
            throw new CompletedProjectException(ScheduleErrorCode.PC_SCHEDULE_NOT_PATCH);
        }
        LocalDateTime now = LocalDateTime.now();
        schedule.updateState(now, scheduleStateUpdateRequest.getState());
        scheduleMemberRepository.updateRecentView(now, schedule.getId());
    }

    @Transactional(readOnly = true) // 프로젝트별 일정 내용
    public ScheduleMyListResponse findMySchedule(ScheduleMonthRequest scheduleMonthRequest, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
        LocalDate localDate = LocalDateTime.now().toLocalDate();
        int lastDay = LocalDateTime.now().toLocalDate().withDayOfMonth(localDate.lengthOfMonth()).getDayOfMonth();
        LocalDateTime startDate = LocalDateTime.of(scheduleMonthRequest.getYear(), scheduleMonthRequest.getMonth(), 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(scheduleMonthRequest.getYear(), scheduleMonthRequest.getMonth(), lastDay, 23, 59, 59);
        List<Schedule> scheduleList = scheduleRepository.findMonthListByMember(memberId, startDate, endDate);
        List<ScheduleMyDto> scheduleDtos = new ArrayList<>();
        Schedule nearSchedule = toMySchedule(member, scheduleList, scheduleDtos);// 일정 리스트를 설정 및 가장 가까운 일정 반환

        ScheduleMyListResponse scheduleMyListResponse = new ScheduleMyListResponse();
        scheduleMyListResponse.setScheduleMyDtos(scheduleDtos);

        ScheduleDto scheduleDto = ScheduleDto.builder()// 가장 가까운 일정을 응답값으로 변환
                .projectId(nearSchedule.getProject().getId())
                .scheduleId(nearSchedule.getId())
                .title(nearSchedule.getTitle())
                .content(nearSchedule.getContent())
                .startDate(nearSchedule.getStartDate())
                .endDate(nearSchedule.getEndDate()).build();
        if (nearSchedule instanceof Meeting) {
            scheduleDto.setAddress(((Meeting) nearSchedule).getAddress());
        }
        scheduleMyListResponse.setNearSchedule(scheduleDto);

        return scheduleMyListResponse;
    }

    private Schedule toMySchedule(Member member, List<Schedule> scheduleList, List<ScheduleMyDto> scheduleDtos) {
        ScheduleMyDto scheduleMyDto = null;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nearDate = now.plusMonths(1l);
        Schedule nearSchedule = scheduleList.get(0);
        for (Schedule schedule : scheduleList) {
            if (now.isAfter(schedule.getStartDate()) && nearDate.isBefore(schedule.getStartDate())) {
                nearDate = schedule.getStartDate();
                nearSchedule = schedule;
            }
            scheduleMyDto = ScheduleMyDto.builder()
                    .projectId(schedule.getProject().getId())
                    .scheduleId(schedule.getId())
                    .title(schedule.getTitle())
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getEndDate())
                    .build();

            List<ScheduleMember> scheduleMembers = schedule.getScheduleMembers();
            for (ScheduleMember scheduleMember : scheduleMembers) {
                if (scheduleMember.getMember() == member && scheduleMember.getRecentView().isBefore(schedule.getModifiedDate())) {
                    System.out.println("확인 못함");
                    scheduleMyDto.updateCheck(false);
                } else if (scheduleMember.getMember() == member) {
                    System.out.println("확인 함");
                    scheduleMyDto.updateCheck(true);
                } else {
                    System.out.println("로그인한 멤버 정보가 아님");
                }
            }
            if (schedule instanceof Meeting) {
                Meeting meeting = (Meeting) schedule;
                scheduleMyDto.setAddress(meeting.getAddress());
                scheduleMyDto.setScheduleCategory(ScheduleCategory.MEETING);
            } else {
                scheduleMyDto.setScheduleCategory(ScheduleCategory.MILESTONE);
            }
            scheduleDtos.add(scheduleMyDto);
        }
        return nearSchedule;
    }

    private ScheduleCalenderMonthResponse toScheduleMonthResponse(List<Schedule> scheduleList) {
        ScheduleCalenderMonthResponse scheduleMonthResponse = new ScheduleCalenderMonthResponse();

        for (Schedule schedule : scheduleList) {
            if (schedule instanceof Meeting) {
                Meeting meeting = (Meeting) schedule;
                //if (meeting.getAddress() != null){
                MeetingDto meetingDto = MeetingDto.builder()
                        .scheduleId(schedule.getId())
                        .title(schedule.getTitle())
                        .content(meeting.getContent())
                        .startDate(meeting.getStartDate())
                        .endDate(meeting.getEndDate())
                        .address(meeting.getAddress()).build();
                List<ScheduleMember> scheduleMembers = schedule.getScheduleMembers();
                scheduleMembers.forEach(sm -> {
                    meetingDto.addMember(sm.getMember());
                });
                scheduleMonthResponse.addMeeting(meetingDto);
                //}
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
                    milestoneDto.addMember(sm.getMember());
                });
                scheduleMonthResponse.addMilestone(milestoneDto);
            }
        }
        return scheduleMonthResponse;
    }

    private List<ScheduleListResponse> toListResponse(LocalDate startDate, List<Schedule> scheduleList, DateCategory dateCategory) {
        System.out.println("dateCategory = " + dateCategory);
        List<ScheduleListResponse> scheduleListResponseList = new ArrayList<>();
        ScheduleListResponse scheduleListResponse = null;
        if (!scheduleList.isEmpty()) {
            int month = startDate.getMonthValue();//scheduleList.get(0).getStartDate().getMonthValue(); //초기값
            LocalDateTime compareDate = scheduleList.get(0).getStartDate();

            Long order = 1l;
            for (Schedule s : scheduleList) {
                if (dateCategory.equals(DateCategory.MONTH)) {
                    if (month < s.getStartDate().getMonth().getValue()) {
                        month = s.getStartDate().getMonth().getValue();
//                        order++;
                        order = ChronoUnit.MONTHS.between(startDate, s.getStartDate()) + 1;
                    }
                } else {
                    order = ChronoUnit.WEEKS.between(startDate, s.getStartDate()) / 2 + 1;
//                    if (ChronoUnit.WEEKS.between(compareDate, s.getStartDate()) > 1) {//* 2주 차이
//                        compareDate = s.getStartDate();
//                        order++;
//                    }
                }
                scheduleListResponse = ScheduleListResponse.builder()
                        .scheduleId(s.getId())
                        .title(s.getTitle())
                        .startDate(s.getStartDate())
                        .endDate(s.getEndDate())
                        .order(order)
                        .build();
                scheduleListResponseList.add(scheduleListResponse);
            }
        }
        return scheduleListResponseList;
    }

    private void addResponse(Page<Schedule> pageSchedule, List<ScheduleMonthResponse> scheduleMonthResponse) {
        LocalDateTime now = LocalDateTime.now();
        for (Schedule schedule : pageSchedule) {
            if (now.isAfter(schedule.getStartDate()) && schedule.getState().equals(State.TBD)) {
                schedule.updateState(now, State.ONGOING);
                for (ScheduleMember scheduleMember : schedule.getScheduleMembers()) {
                    scheduleMember.updateRecentView(now);
                }

//                scheduleMemberRepository.updateRecentView(now, schedule.getId());// 벌크로 수정한게 오히려 손해 -> flush, clear 하면 schedule 준영속상태로 다시 find로 조회해서 영속성 찾아야함으로 쿼리가 오히려 더 나갈 수 있음
            }


            ScheduleMonthResponse scheduleResponse = ScheduleMonthResponse.builder()
                    .scheduleId(schedule.getId())
                    .title(schedule.getTitle())
                    .startDate(schedule.getStartDate())
                    .endDate(schedule.getEndDate())
                    .build();


            List<ScheduleMember> scheduleMembers = schedule.getScheduleMembers();
            scheduleMembers.forEach(sm -> {
                scheduleResponse.addMember(sm.getMember());
            });
            if (schedule instanceof Meeting) {
                scheduleResponse.setAddress(((Meeting) schedule).getAddress());
                scheduleResponse.setScheduleCategory(ScheduleCategory.MEETING);
            } else {
                scheduleResponse.setScheduleCategory(ScheduleCategory.MILESTONE);
            }
            scheduleMonthResponse.add(scheduleResponse);

        }
    }

    private List<ScheduleSearchResponse> createPageResponse(List<Schedule> scheduleList, List<ScheduleSearchResponse> scheduleSearchResponse, Member member, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();

        for (Schedule schedule : scheduleList) {
            if (now.isAfter(schedule.getStartDate()) && schedule.getState().equals(State.TBD)) {
                schedule.updateState(now, State.ONGOING);
                for (ScheduleMember scheduleMember : schedule.getScheduleMembers()) {
                    scheduleMember.updateRecentView(now);
                }
            }


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
                //scheduleResponse.setAddress(scheduleResponse.getAddress());
                scheduleResponse.setScheduleCategory(ScheduleCategory.MEETING);
            } else {
                scheduleResponse.setScheduleCategory(ScheduleCategory.MILESTONE);
            }


            boolean eval = midtermEvaluationRepository.existsByScheduleIdAndVoterId(schedule.getId(), member.getId());
            if (!eval && schedule.getState().equals(State.COMPLETE)) { //? 평가한 적이 없는 완료된 일정을 고름
                if (schedule.getScheduleMembers().stream().anyMatch(sm -> sm.getMember().getId() == member.getId())) {//? 사용자가 일정에 들어갔는지 확인
                    scheduleResponse.setEvaluationRequired(true);
                }
            }


            scheduleSearchResponse.add(scheduleResponse);

        }
        List<ScheduleSearchResponse> newResponse = new ArrayList<>();
        for (ScheduleSearchResponse response : scheduleSearchResponse) {
            if (!(response.getEndDate().isBefore(LocalDateTime.now()) && !response.getEvaluationRequired())) {//? 현재 이후 날짜이거나 내가 평가할 필요가 있는 일정일 경우
                newResponse.add(response);
            }
        }


        return newResponse;
    }

    private void checkModify(Member member, Schedule schedule, ScheduleSearchResponse scheduleResponse) {
        List<ScheduleMember> scheduleMembers = schedule.getScheduleMembers();
        scheduleMembers.forEach(sm -> {
            scheduleResponse.addMember(sm.getMember());
//            System.out.println("------------------------------------------------");
//            System.out.println("sm.getRecentView() = " + sm.getRecentView());
//            System.out.println("schedule.getModifiedDate() = " + schedule.getModifiedDate());
//            System.out.println("sm.getRecentView().isBefore(schedule.getModifiedDate()) = " + sm.getRecentView().isBefore(schedule.getModifiedDate()));
//            System.out.println("------------------------------------------------");
            if (sm.getMember() == member && sm.getRecentView().isBefore(schedule.getModifiedDate())) {
//                    log.info("ChronoUnit.SECONDS.between(sm.getRecentView(),schedule.getModifiedDate()); = " + ChronoUnit.SECONDS.between(sm.getRecentView(),schedule.getModifiedDate()));
                //System.out.println("확인 못함" );
                scheduleResponse.updateCheck(false);
            } else if (sm.getMember() == member) {
                //System.out.println("확인 함" );
                scheduleResponse.updateCheck(true);
            } else {
                //System.out.println("로그인한 멤버 정보가 아님");
            }
        });
    }

    @Transactional
    public ScheduleDetailResponse findSchedule(Long projectId, Long scheduleId, Long memberId) {
        Project project = projectRepository.findProjectWithPMAndMemberById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
        Schedule schedule = scheduleRepository.findJoinSMById(scheduleId).orElseThrow(() -> new EntityNotFoundException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        List<ProjectMember> projectMembers = project.getProjectMembers();
        List<ScheduleMember> scheduleMembers = schedule.getScheduleMembers();

        ProjectMember pmMe = projectMembers.stream().filter(pm -> pm.getMember().getId() == memberId).findFirst().get();

        ScheduleDetailResponse scheduleDetailResponse = ScheduleDetailResponse.builder()
                .projectId(projectId)
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .deleteAuthorization(validateDelete(scheduleId, projectId, memberId, pmMe))
                .build();

        for (ProjectMember projectMember : projectMembers) {
            DetailMemberDto detailMemberDto = DetailMemberDto.builder().id(projectMember.getMember().getId())
                    .name(projectMember.getMember().getName()).build();
            for (ScheduleMember scheduleMember : scheduleMembers) {
                if (projectMember.getMember() == scheduleMember.getMember()) {
                    detailMemberDto.setIn(true);
                }
                if (projectMember.getMember() == scheduleMember.getMember() && projectMember.getMember().getId() == memberId) { // 로그인한 사용자 최근 확인한 일정 갱신
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

}
