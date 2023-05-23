package fullcare.backend.schedule.service;

import fullcare.backend.global.State;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import fullcare.backend.schedule.ScheduleCategory;
import fullcare.backend.schedule.domain.Meeting;
import fullcare.backend.schedule.domain.Milestone;
import fullcare.backend.schedule.domain.Schedule;
import fullcare.backend.schedule.dto.*;
import fullcare.backend.schedule.dto.request.*;
import fullcare.backend.schedule.dto.response.*;
import fullcare.backend.schedule.repository.ScheduleRepository;
import fullcare.backend.schedulemember.domain.ScheduleMember;
import fullcare.backend.schedulemember.repository.ScheduleMemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public boolean updateSchedule(ScheduleUpdateRequest scheduleUpdateRequest, Long scheduleId) {// 멤버 로그인 사용자 검증 수정
        Schedule schedule = scheduleRepository.findJoinSMById(scheduleId).orElseThrow();
        if ((schedule instanceof Meeting && scheduleUpdateRequest.getCategory().equals(ScheduleCategory.MILESTONE)) || (schedule instanceof Milestone && scheduleUpdateRequest.getCategory().equals(ScheduleCategory.MEETING)) ){
            throw new RuntimeException("수정하려는 일정의 카테고리가 맞지 않습니다.");
        }
        List<ProjectMember> pmList = projectMemberRepository.findByProjectId(scheduleUpdateRequest.getProjectId());
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
        if (!members.containsAll(updateMemberList)){// 프로젝트에 속한 사람인지 확인
            return false;}



        schedule.getScheduleMembers().clear(); // 어떤 사람이 들어오고, 나가고, 그대로 있는지를 파악을 해야함,
        for (Member member : updateMemberList) {
            schedule.addMember(member);
        }

        if (scheduleUpdateRequest.getCategory().equals(ScheduleCategory.MEETING)){ // Meeting
            Meeting meeting = (Meeting)schedule;
            meeting.updateAddress(scheduleUpdateRequest.getAddress());
        }else{ // Milestone
        }
        return true;
    }
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();
        scheduleRepository.delete(schedule);
    }
    @Transactional(readOnly = true)
    public CustomResponseDto findScheduleList(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        LocalDate startDate = project.getStartDate();
        LocalDate endDate = project.getEndDate();

        //schedule로 통합 필요
//        List<Schedule> meetingList = scheduleRepository.findMeetingByProjectId(projectId); // dtype을 구분 짓기 위해 각 메소드를 만듬
//        List<ScheduleListResponse> scheduleListResponseList = toListResponse(meetingList, ScheduleCategory.미팅);

        List<Schedule> milestoneList = scheduleRepository.findMileStoneByProjectId(projectId);
        List<ScheduleListResponse> scheduleListResponseList = toListResponse(milestoneList,ScheduleCategory.MILESTONE);
        /////////////////
        //scheduleListResponseList.addAll(response2);

        scheduleListResponseList.sort(Comparator.comparing(ScheduleListResponse::getStartDate));// 날짜 기준 내림차순 정렬

        CustomResponseDto response = new CustomResponseDto(startDate, endDate, scheduleListResponseList);

        return response;
    }

    @Transactional(readOnly = true)
    public ScheduleCalenderMonthResponse findScheduleCalenderList(int year, int month) { // 1일부터 31일까지 일정
        LocalDate localDate = LocalDateTime.now().toLocalDate();
        int lastDay = LocalDateTime.now().toLocalDate().withDayOfMonth(localDate.lengthOfMonth()).getDayOfMonth();
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0,0);
        LocalDateTime endDate = LocalDateTime.of(year, month,lastDay,23,59,59  );

        List<Schedule> scheduleList = scheduleRepository.findByStartDateBetweenOrEndDateBetween( startDate, endDate,startDate, endDate  );
        ScheduleCalenderMonthResponse scheduleMonthResponse = toScheduleMonthResponse(scheduleList);
        scheduleMonthResponse.getMeetings().sort(Comparator.comparing(MeetingDto::getStartDate));// 날짜 기준 내림차순 정렬
        scheduleMonthResponse.getMilestones().sort(Comparator.comparing(MilestoneDto::getStartDate));// 날짜 기준 내림차순 정렬
        return scheduleMonthResponse;
    }
    @Transactional
    public Page<ScheduleMonthResponse> findScheduleMonthList(Pageable pageable, int year, int month, Member member, List<State> states) { // 1일부터 31일까지 일정
        Member findMember = memberRepository.findById(member.getId()).orElseThrow();

        LocalDate localDate = LocalDateTime.now().toLocalDate();
        int lastDay = LocalDateTime.now().toLocalDate().withDayOfMonth(localDate.lengthOfMonth()).getDayOfMonth();
        LocalDateTime startDate = LocalDateTime.of(year,month, 1, 0, 0,0);
        LocalDateTime endDate = LocalDateTime.of(year, month,lastDay,23,59,59  );
        Page<Schedule> pageSchedule = scheduleRepository.findMonthByStartDateBetweenOrEndDateBetween(pageable, startDate, endDate, startDate, endDate, states);
        List<ScheduleMonthResponse> scheduleMonthResponses = new ArrayList<>();
        addResponse(pageSchedule, scheduleMonthResponses, findMember);// 미팅, 마일스톤에 맞게 일정 생성 후 응답에 넣기
        return new PageImpl<>(scheduleMonthResponses, pageable, scheduleMonthResponses.size());
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
    public void updateState(ScheduleStateUpdateRequest scheduleStateUpdateRequest, Long scheduleId){ // 상태 바꿀 때도 schedulemember recentview 바꿔야함
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();
        LocalDateTime now = LocalDateTime.now();
        schedule.updateState(now, scheduleStateUpdateRequest.getState());
        scheduleMemberRepository.updateRecentView(now, schedule.getId());
    }
    @Transactional(readOnly = true) // 프로젝트별 일정 내용
    public ScheduleMyListResponse findMySchedule(ScheduleMonthRequest scheduleMonthRequest, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        LocalDate localDate = LocalDateTime.now().toLocalDate();
        int lastDay = LocalDateTime.now().toLocalDate().withDayOfMonth(localDate.lengthOfMonth()).getDayOfMonth();
        LocalDateTime startDate = LocalDateTime.of(scheduleMonthRequest.getYear(),scheduleMonthRequest.getMonth(), 1, 0, 0,0);
        LocalDateTime endDate = LocalDateTime.of(scheduleMonthRequest.getYear(), scheduleMonthRequest.getMonth(),lastDay,23,59,59  );
        List<Schedule> scheduleList = scheduleRepository.findMonthListByMember( memberId, startDate, endDate);
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
        if(nearSchedule instanceof Meeting){
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
            if(now.isAfter(schedule.getStartDate()) && nearDate.isBefore(schedule.getStartDate())){
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
                if(scheduleMember.getMember() == member && scheduleMember.getRecentView().isBefore(schedule.getModifiedDate())){
                    System.out.println("확인 못함" );
                    scheduleMyDto.updateCheck(false);
                }else if (scheduleMember.getMember() == member){
                    System.out.println("확인 함" );
                    scheduleMyDto.updateCheck(true);
                }else{
                    System.out.println("로그인한 멤버 정보가 아님");
                }
            }
            if (schedule instanceof Meeting){
                Meeting meeting = (Meeting) schedule;
                scheduleMyDto.setAddress(meeting.getAddress());
                scheduleMyDto.setScheduleCategory(ScheduleCategory.MEETING);
            }else{
                scheduleMyDto.setScheduleCategory(ScheduleCategory.MILESTONE);
            }
            scheduleDtos.add(scheduleMyDto);
        }
        return nearSchedule;
    }

    private ScheduleCalenderMonthResponse toScheduleMonthResponse(List<Schedule> scheduleList) {
        ScheduleCalenderMonthResponse scheduleMonthResponse = new ScheduleCalenderMonthResponse();

        for (Schedule schedule : scheduleList) {
            if (schedule instanceof Meeting){
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
            }else{
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


    private List<ScheduleListResponse> toListResponse(List<Schedule> scheduleList, ScheduleCategory scheduleCategory) {
        return scheduleList.stream().map(s -> ScheduleListResponse.builder()
                .scheduleId(s.getId())
                .title(s.getTitle())
                .content(s.getContent())
                .startDate(s.getStartDate())
                .category(scheduleCategory.name())
                .build()
        ).collect(Collectors.toList());
    }


    private void addResponse(Page<Schedule> pageSchedule, List<ScheduleMonthResponse> scheduleMonthResponses, Member member) {
        LocalDateTime now = LocalDateTime.now();
        for (Schedule schedule : pageSchedule) {
            if (now.isAfter(schedule.getStartDate()) && schedule.getState().equals(State.TBD)){
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
                    .state(schedule.getState())
                    .modifyDate(schedule.getModifiedDate().toLocalDate())
                    .build();


            checkModify(member, schedule, scheduleResponse);
            if (schedule instanceof Meeting){
                //scheduleResponse.setAddress(scheduleResponse.getAddress());
                scheduleResponse.setScheduleCategory(ScheduleCategory.MEETING);
            }else{
                scheduleResponse.setScheduleCategory(ScheduleCategory.MILESTONE);
            }
            scheduleMonthResponses.add(scheduleResponse);

        }
    }

    private void checkModify(Member member, Schedule schedule, ScheduleMonthResponse scheduleResponse) {
        List<ScheduleMember> scheduleMembers = schedule.getScheduleMembers();
        scheduleMembers.forEach(sm -> {
            scheduleResponse.addMember(sm.getMember());
//            System.out.println("------------------------------------------------");
//            System.out.println("sm.getRecentView() = " + sm.getRecentView());
//            System.out.println("schedule.getModifiedDate() = " + schedule.getModifiedDate());
//            System.out.println("sm.getRecentView().isBefore(schedule.getModifiedDate()) = " + sm.getRecentView().isBefore(schedule.getModifiedDate()));
//            System.out.println("------------------------------------------------");
                if(sm.getMember() == member && sm.getRecentView().isBefore(schedule.getModifiedDate())){
//                    log.info("ChronoUnit.SECONDS.between(sm.getRecentView(),schedule.getModifiedDate()); = " + ChronoUnit.SECONDS.between(sm.getRecentView(),schedule.getModifiedDate()));
                    //System.out.println("확인 못함" );
                    scheduleResponse.updateCheck(false);
                }else if (sm.getMember() == member){
                    //System.out.println("확인 함" );
                    scheduleResponse.updateCheck(true);
                }else{
                    //System.out.println("로그인한 멤버 정보가 아님");
                }
            });
    }

    @Transactional
    public ScheduleDetailResponse findSchedule(Long projectId, Long scheduleId, Long memberId) {
        Project project = projectRepository.findJoinPMJoinMemberById(projectId).orElseThrow();
        Schedule schedule = scheduleRepository.findJoinSMById(scheduleId).orElseThrow();
        List<ProjectMember> projectMembers = project.getProjectMembers();
        List<ScheduleMember> scheduleMembers = schedule.getScheduleMembers();

        ScheduleDetailResponse scheduleDetailResponse = ScheduleDetailResponse.builder()
                .projectId(projectId)
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .build();
        for (ProjectMember projectMember : projectMembers) {
            DetailMemberDto detailMemberDto = DetailMemberDto.builder().id(projectMember.getMember().getId())
                    .name(projectMember.getMember().getName()).build();
            for (ScheduleMember scheduleMember : scheduleMembers) {
                if (projectMember.getMember() == scheduleMember.getMember()){
                    detailMemberDto.setIn(true);
                }
                if(projectMember.getMember() == scheduleMember.getMember() && projectMember.getMember().getId() == memberId){ // 로그인한 사용자 최근 확인한 일정 갱신
                    scheduleMember.updateRecentView(LocalDateTime.now());
                }
            }
            scheduleDetailResponse.addMember(detailMemberDto);
        }

        if(schedule instanceof Meeting){
            scheduleDetailResponse.setScheduleCategory(ScheduleCategory.MEETING);
            scheduleDetailResponse.setAddress(scheduleDetailResponse.getAddress());
        }else{
            scheduleDetailResponse.setScheduleCategory(ScheduleCategory.MILESTONE);
        }



        return scheduleDetailResponse;

    }

}
