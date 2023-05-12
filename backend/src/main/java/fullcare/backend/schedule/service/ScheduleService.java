package fullcare.backend.schedule.service;

import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import fullcare.backend.schedule.ScheduleCategory;
import fullcare.backend.schedule.domain.Meeting;
import fullcare.backend.schedule.domain.Schedule;
import fullcare.backend.schedule.dto.MeetingDto;
import fullcare.backend.schedule.dto.MilestoneDto;
import fullcare.backend.schedule.dto.request.ScheduleDeleteRequest;
import fullcare.backend.schedule.dto.request.ScheduleMonthRequest;
import fullcare.backend.schedule.dto.request.ScheduleStateUpdateRequest;
import fullcare.backend.schedule.dto.request.ScheduleUpdateRequest;
import fullcare.backend.schedule.dto.response.CustomResponseDto;
import fullcare.backend.schedule.dto.response.ScheduleListResponse;
import fullcare.backend.schedule.dto.response.ScheduleMonthResponse;
import fullcare.backend.schedule.dto.response.ScheduleServingResponse;
import fullcare.backend.schedule.repository.ScheduleRepository;
import fullcare.backend.schedulemember.domain.ScheduleMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @Transactional(readOnly = true)
    public List<ScheduleServingResponse> servingMember(Long projectId){
//        Project project = projectRepository.findById(projectId).orElseThrow();
//        List<ScheduleServingResponse> response = project.getProjectMembers().stream().map(pms -> ScheduleServingResponse.builder()
//                .id(pms.getMember().getId())
//                .name(pms.getMember().getName()).build()
//        ).collect(Collectors.toList());

        List<ProjectMember> pmList = projectMemberRepository.findByProjectId(projectId);
        if (pmList.size()==0){
            throw new RuntimeException("프로젝트 조회 불가");
        }
        List<ScheduleServingResponse> response = pmList.stream().map(pms -> ScheduleServingResponse.builder()
                .id(pms.getMember().getId())
                .name(pms.getMember().getName()).build()).collect(Collectors.toList());
        return response;
    }
    public boolean updateSchedule(ScheduleUpdateRequest scheduleUpdateRequest, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Schedule schedule = scheduleRepository.findById(scheduleUpdateRequest.getScheduleId()).orElseThrow();
        Project project = projectRepository.findById(scheduleUpdateRequest.getProjectId()).orElseThrow();
        List<Member> newMemberList = new ArrayList<>();
        List<Member> currentProjectMembers = project.getProjectMembers().stream().map(pm -> pm.getMember()).collect(Collectors.toList());

//        List<Member> currentScheduleMembers = schedule.getScheduleMembers().stream().map(sm -> sm.getMember()).collect(Collectors.toList());
        if(currentProjectMembers.contains(member)){
            schedule.update(
                    scheduleUpdateRequest.getState(),
                    scheduleUpdateRequest.getTitle(),
                    scheduleUpdateRequest.getContent(),
                    scheduleUpdateRequest.getStartDate(),
                    scheduleUpdateRequest.getEndDate()
            );
            for (Long scheduleMemberId : scheduleUpdateRequest.getMemberIds()) {
                Member scheduleMember = memberRepository.findById(scheduleMemberId).orElseThrow();
                if (!currentProjectMembers.contains(scheduleMember)){// 프로젝트에 속한 사람인지 확인
                    return false;}
                newMemberList.add(scheduleMember);
            }

            schedule.getScheduleMembers().clear();
            schedule.addMemberList(newMemberList);


            if (scheduleUpdateRequest.getCategory().equals(ScheduleCategory.미팅)){ // Meeting
                Meeting meeting = (Meeting)schedule;
                meeting.updateAddress(scheduleUpdateRequest.getAddress());
            }else{ // Milestone
            }




        }else{ // 권한 없는 사람이면 실패
            System.out.println("권한 없음");
            return false;
        }
        return true;
    }
    public boolean deleteSchedule(ScheduleDeleteRequest scheduleDeleteRequest, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Project project = projectRepository.findById(scheduleDeleteRequest.getProjectId()).orElseThrow();
        Schedule schedule = scheduleRepository.findById(scheduleDeleteRequest.getScheduleId()).orElseThrow();
        List<Member> currentProjectMembers = project.getProjectMembers().stream().map(pm -> pm.getMember()).collect(Collectors.toList());
        if(currentProjectMembers.contains(member)){
            scheduleRepository.delete(schedule);
            return true;
        }
        return false;
    }
    @Transactional(readOnly = true)
    public CustomResponseDto<ScheduleListResponse> findScheduleList(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        LocalDate startDate = project.getStartDate();
        LocalDate endDate = project.getEndDate();

        //schedule로 통합 필요
        List<Schedule> meetingList = scheduleRepository.findMeetingByProjectId(projectId); // dtype을 구분 짓기 위해 각 메소드를 만듬
        List<ScheduleListResponse> scheduleListResponseList = toListResponse(meetingList, ScheduleCategory.미팅);

        List<Schedule> milestoneList = scheduleRepository.findMileStoneByProjectId(projectId);
        List<ScheduleListResponse> response2 = toListResponse(milestoneList,ScheduleCategory.개발일정);
        /////////////////
        scheduleListResponseList.addAll(response2);

        scheduleListResponseList.sort(Comparator.comparing(ScheduleListResponse::getStartDate));// 날짜 기준 내림차순 정렬

        CustomResponseDto<ScheduleListResponse> response = new CustomResponseDto<>(startDate, endDate, scheduleListResponseList);

        return response;
    }

    @Transactional(readOnly = true)
    public ScheduleMonthResponse findScheduleMonthList(ScheduleMonthRequest scheduleMonthRequest) {
        LocalDate localDate = LocalDateTime.now().toLocalDate();
        int lastDay = LocalDateTime.now().toLocalDate().withDayOfMonth(localDate.lengthOfMonth()).getDayOfMonth();
        LocalDateTime startDate = LocalDateTime.of(scheduleMonthRequest.getYear(),scheduleMonthRequest.getMonth(), 1, 0, 0,0);
        LocalDateTime endDate = LocalDateTime.of(scheduleMonthRequest.getYear(), scheduleMonthRequest.getMonth(),lastDay,23,59,59  );

        List<Schedule> scheduleList = scheduleRepository.findByStartDateBetweenOrEndDateBetween(startDate, endDate, startDate, endDate);

        ScheduleMonthResponse scheduleMonthResponse = toScheduleMonthResponse(scheduleList);
        scheduleMonthResponse.getMeetings().sort(Comparator.comparing(MeetingDto::getStartDate));// 날짜 기준 내림차순 정렬
        scheduleMonthResponse.getMilestones().sort(Comparator.comparing(MilestoneDto::getStartDate));// 날짜 기준 내림차순 정렬
        return scheduleMonthResponse;
    }

    public boolean updateState(ScheduleStateUpdateRequest scheduleStateUpdateRequest, Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow();
        Project project = projectRepository.findById(scheduleStateUpdateRequest.getProjectId()).orElseThrow();
        Schedule schedule = scheduleRepository.findById(scheduleStateUpdateRequest.getScheduleId()).orElseThrow();
        List<Member> currentProjectMembers = project.getProjectMembers().stream().map(pm -> pm.getMember()).collect(Collectors.toList());
//        List<Member> currentScheduleMembers = schedule.getScheduleMembers().stream().map(sm -> sm.getMember()).collect(Collectors.toList());
        if (currentProjectMembers.contains(member)){
            schedule.updateState(scheduleStateUpdateRequest.getState());
            return true;
        }
        return false;
    }
    @Transactional(readOnly = true)
    public void findMySchedule(ScheduleMonthRequest scheduleMonthRequest, Long memberId) {
        LocalDate localDate = LocalDateTime.now().toLocalDate();
        int lastDay = LocalDateTime.now().toLocalDate().withDayOfMonth(localDate.lengthOfMonth()).getDayOfMonth();
        LocalDateTime startDate = LocalDateTime.of(scheduleMonthRequest.getYear(),scheduleMonthRequest.getMonth(), 1, 0, 0,0);
        LocalDateTime endDate = LocalDateTime.of(scheduleMonthRequest.getYear(), scheduleMonthRequest.getMonth(),lastDay,23,59,59  );
        List<Schedule> scheduleList = scheduleRepository.findMonthListByMember( memberId, startDate, endDate);

    }

    private ScheduleMonthResponse toScheduleMonthResponse(List<Schedule> scheduleList) {
        ScheduleMonthResponse scheduleMonthResponse = new ScheduleMonthResponse();

        for (Schedule schedule : scheduleList) {
            Meeting meeting = (Meeting) schedule;
            if (meeting.getAddress() != null){
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
            }else{
                MilestoneDto milestoneDto = MilestoneDto.builder()
                        .scheduleId(schedule.getId())
                        .title(schedule.getTitle())
                        .content(meeting.getContent())
                        .startDate(meeting.getStartDate())
                        .endDate(meeting.getEndDate())
                        .build();
                List<ScheduleMember> scheduleMembers = schedule.getScheduleMembers();
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



}
