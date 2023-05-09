package fullcare.backend.project.service;

import fullcare.backend.global.State;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.ProjectCreateRequest;
import fullcare.backend.project.dto.ProjectListResponse;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.projectmember.domain.ProjectMemberRole;
import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    @Transactional(readOnly = true)
    public Page<ProjectListResponse> findMyProjectList(Pageable pageable, Long memberId, List<State> states) {
        Page<Project> pageProject = projectRepository.findProjectList(pageable, memberId, states);

        List<ProjectListResponse> content = pageProject.stream().map(p -> ProjectListResponse.builder()
                .projectId(p.getId())
                .title(p.getTitle())
                .content(p.getContent().length() > 20 ? p.getContent().substring(0, 20) + "..." : p.getContent())
                .build()
        ).collect(Collectors.toList());
        return new PageImpl<>(content, pageable, content.size());
    }

    public void createProject(Long memberId, ProjectCreateRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow();

        Project newProject = Project.createNewProject()
                .title(request.getTitle())
                .content(request.getContent())
                .state(State.진행중)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        

        newProject.addMember(member, new ProjectMemberRole(ProjectMemberRoleType.리더, ProjectMemberRoleType.미정));

        projectRepository.save(newProject);
    }
}
