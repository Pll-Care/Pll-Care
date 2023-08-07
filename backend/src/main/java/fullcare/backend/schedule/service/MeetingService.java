package fullcare.backend.schedule.service;

import fullcare.backend.global.State;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.CompletedProjectException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.project.service.ProjectService;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import fullcare.backend.schedule.domain.Meeting;
import fullcare.backend.schedule.domain.Schedule;
import fullcare.backend.schedule.dto.request.ScheduleCreateRequest;
import fullcare.backend.schedule.repository.MeetingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static fullcare.backend.global.errorcode.ScheduleErrorCode.INVALID_CREATE;
import static fullcare.backend.global.errorcode.ScheduleErrorCode.INVALID_MODIFY;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MeetingService {
    private final MemberRepository memberRepository;
    private final MeetingRepository meetingRepository;
    private final ProjectService projectService;

    public void createMeeting(ScheduleCreateRequest scheduleCreateRequest, Long memberId) {
        try {
            ProjectMember projectMember = projectService.isProjectAvailable(scheduleCreateRequest.getProjectId(), memberId, false);
            LocalDateTime now = LocalDateTime.now();
            Project project = projectMember.getProject();
            Member author = projectMember.getMember();
            LocalDateTime startDate = project.getStartDate().atStartOfDay();
            LocalDateTime endDate = project.getEndDate().atStartOfDay();
            Schedule.validDate(startDate, endDate, scheduleCreateRequest.getStartDate(), scheduleCreateRequest.getEndDate());
            List<Long> memberIds = scheduleCreateRequest.getMemberIds();
            List<Member> memberList = new ArrayList<>();
            memberIds.forEach(m -> {
                Member member = memberRepository.findById(m).orElseThrow(() -> new EntityNotFoundException("해당 사용자가 존재하지 않습니다."));
                memberList.add(member);
            });
            Meeting meeting = Meeting.builder()
                    .project(project)
                    .startDate(scheduleCreateRequest.getStartDate())
                    .endDate(scheduleCreateRequest.getEndDate())
                    .title(scheduleCreateRequest.getTitle())
                    .content(scheduleCreateRequest.getContent())
                    .member(author)
                    .state(State.TBD)
                    .address(scheduleCreateRequest.getAddress())
                    .createdDate(now)
                    .modifiedDate(now)
                    .build();

            meeting.addMemberList(memberList);
        meetingRepository.save(meeting);
        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(INVALID_CREATE);
        }
    }
}
