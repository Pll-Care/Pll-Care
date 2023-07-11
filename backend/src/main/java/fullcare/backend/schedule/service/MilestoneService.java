package fullcare.backend.schedule.service;

import fullcare.backend.global.State;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.CompletedProjectException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import fullcare.backend.schedule.domain.Milestone;
import fullcare.backend.schedule.domain.Schedule;
import fullcare.backend.schedule.dto.request.ScheduleCreateRequest;
import fullcare.backend.schedule.repository.MilestoneRepository;
import jakarta.persistence.EntityNotFoundException;
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
public class MilestoneService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final MilestoneRepository milestoneRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public void createMilestone(ScheduleCreateRequest scheduleCreateRequest, Member author) {
        LocalDateTime now = LocalDateTime.now();
        Project project = projectRepository.findById(scheduleCreateRequest.getProjectId()).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트가 존재하지 않습니다."));
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndMemberId(scheduleCreateRequest.getProjectId(), author.getId()).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트 멤버가 존재하지 않습니다."));
        LocalDateTime startDate = project.getStartDate().atStartOfDay();
        LocalDateTime endDate = project.getEndDate().atStartOfDay();
        Schedule.validDate(startDate, endDate, scheduleCreateRequest.getStartDate(), scheduleCreateRequest.getEndDate());
        if (project.isCompleted()) {
            throw new CompletedProjectException(ProjectErrorCode.PROJECT_COMPLETED); // todo "완료된 프로젝트는 일정을 생성하지 못합니다."
        }
        List<Long> memberIds = scheduleCreateRequest.getMemberIds();
        List<Member> memberList = new ArrayList<>();
        memberIds.forEach(m -> {
            Member member = memberRepository.findById(m).orElseThrow(() -> new EntityNotFoundException("해당 사용자가 존재하지 않습니다."));
            memberList.add(member);
        });
        Milestone milestone = Milestone.builder()
                .project(project)
                .startDate(scheduleCreateRequest.getStartDate())
                .endDate(scheduleCreateRequest.getEndDate())
                .title(scheduleCreateRequest.getTitle())
                .content(scheduleCreateRequest.getContent())
                .member(author)
//                .state(State.예정)
                .createdDate(now)
                .modifiedDate(now)
                .state(State.TBD)
                .build();
        milestone.addMemberList(memberList);

        milestoneRepository.save(milestone);

    }
}
