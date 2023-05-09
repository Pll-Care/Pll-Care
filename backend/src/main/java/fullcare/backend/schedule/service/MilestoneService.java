package fullcare.backend.schedule.service;

import fullcare.backend.global.State;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.schedule.domain.Milestone;
import fullcare.backend.schedule.domain.Schedule;
import fullcare.backend.schedule.dto.MemberDto;
import fullcare.backend.schedule.dto.request.ScheduleCreateRequest;
import fullcare.backend.schedule.repository.MilestoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void createMilestone(ScheduleCreateRequest scheduleCreateRequest, String username) {
        Project project = projectRepository.findById(scheduleCreateRequest.getProjectId()).orElseThrow();
        List<MemberDto> memberDtos = scheduleCreateRequest.getMemberDtos();
        List<Member> memberList = new ArrayList<>();
        memberDtos.forEach(m -> {
            Member member = memberRepository.findById(m.getId()).orElseThrow();
            memberList.add(member);
        });
        Milestone milestone = Milestone.builder()
                .project(project)
                .startDate(scheduleCreateRequest.getStartDate())
                .endDate(scheduleCreateRequest.getEndDate())
                .title(scheduleCreateRequest.getTitle())
                .content(scheduleCreateRequest.getContent())
                .author(username)
                .state(State.예정)
                .build();
        milestone.addMemberList(memberList);

        milestoneRepository.save(milestone);

    }
}
