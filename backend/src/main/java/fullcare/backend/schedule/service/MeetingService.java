package fullcare.backend.schedule.service;

import fullcare.backend.global.State;
import fullcare.backend.global.errorcode.MemberErrorCode;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.CompletedProjectException;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
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
    private final ProjectMemberRepository projectMemberRepository;
    private final MeetingRepository meetingRepository;
    private final ProjectService projectService;

    public void createMeeting(ScheduleCreateRequest scheduleCreateRequest, Long memberId) {
        try {
            ProjectMember projectMember = projectService.isProjectAvailable(scheduleCreateRequest.getProjectId(), memberId, false);
            LocalDateTime now = LocalDateTime.now();
            Project project = projectMember.getProject();
            LocalDateTime startDate = project.getStartDate().atStartOfDay();
            LocalDateTime endDate = project.getEndDate().atStartOfDay();
            Schedule.validDate(startDate, endDate, scheduleCreateRequest.getStartDate(), scheduleCreateRequest.getEndDate());
            List<Long> pmIds = scheduleCreateRequest.getPmIds();
            List<ProjectMember> pmList = new ArrayList<>();
            pmIds.forEach(pmId -> {
                ProjectMember pm = projectMemberRepository.findById(pmId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
                pmList.add(pm);
            });
            Meeting meeting = Meeting.builder()
                    .project(project)
                    .startDate(scheduleCreateRequest.getStartDate())
                    .endDate(scheduleCreateRequest.getEndDate())
                    .title(scheduleCreateRequest.getTitle())
                    .content(scheduleCreateRequest.getContent())
                    .author(projectMember)
                    .state(State.TBD)
                    .address(scheduleCreateRequest.getAddress())
                    .createdDate(now)
                    .modifiedDate(now)
                    .build();

            meeting.addMemberList(pmList);
        meetingRepository.save(meeting);
        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(INVALID_CREATE);
        }
    }
}
