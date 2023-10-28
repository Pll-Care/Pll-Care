package fullcare.backend.schedule.service;

import fullcare.backend.global.State;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.schedule.domain.Meeting;
import fullcare.backend.schedule.domain.Schedule;
import fullcare.backend.schedule.dto.MemberDto;
import fullcare.backend.schedule.dto.request.ScheduleCreateRequest;
import fullcare.backend.schedule.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MeetingService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final MeetingRepository meetingRepository;

    public void createMeeting(ScheduleCreateRequest scheduleCreateRequest, String username) {
        LocalDateTime now = LocalDateTime.now();
        Project project = projectRepository.findById(scheduleCreateRequest.getProjectId()).orElseThrow();
        LocalDateTime startDate = project.getStartDate().atStartOfDay();
        LocalDateTime endDate = project.getEndDate().atStartOfDay();
        Schedule.validDate(startDate, endDate, scheduleCreateRequest.getStartDate(), scheduleCreateRequest.getEndDate());

        List<MemberDto> memberDtos = scheduleCreateRequest.getMemberDtos();
        List<Member> memberList = new ArrayList<>();
        memberDtos.forEach(m -> {
            Member member = memberRepository.findById(m.getId()).orElseThrow();
            memberList.add(member);
        });
        Meeting meeting = Meeting.builder()
                .project(project)
                .startDate(scheduleCreateRequest.getStartDate())
                .endDate(scheduleCreateRequest.getEndDate())
                .title(scheduleCreateRequest.getTitle())
                .content(scheduleCreateRequest.getContent())
                .author(username)
                .state(State.TBD)
                .address(scheduleCreateRequest.getAddress())
                .createdDate(now)
                .modifiedDate(now)
                .build();

        meeting.addMemberList(memberList);
        meetingRepository.save(meeting);

    }
}
