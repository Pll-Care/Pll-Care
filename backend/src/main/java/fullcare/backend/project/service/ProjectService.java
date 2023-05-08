package fullcare.backend.project.service;

import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.domain.State;
import fullcare.backend.project.dto.ProjectCreateRequest;
import fullcare.backend.project.dto.ProjectListResponse;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.projectmember.domain.ProjectMemberRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public Page<ProjectListResponse> findList(Pageable pageable, Long memberId, List<State> states){
//        List<State> state = new ArrayList<>();
//        state.add(State.예정);
//        state.add(State.진행중);
        Page<Project> pageProject = projectRepository.findProjectList(pageable, memberId, states);
        List<ProjectListResponse> content = pageProject.stream().map(p -> ProjectListResponse.builder()
                .projectId(p.getId())
                .title(p.getTitle())
                .content(p.getContent().length() > 20 ? p.getContent().substring(0,20)+"..." : p.getContent())
                .build()
        ).collect(Collectors.toList());
        return new PageImpl<>(content, pageable, content.size());
    }

    public void createProject(ProjectCreateRequest request, Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow();
        LocalDateTime createdDate = LocalDateTime.now();
        Project project = new Project(request);
        project.updateState(State.예정);
        project.setCreateDate(createdDate);
        project.addMember(member, ProjectMemberRole.리더);
        projectRepository.save(project);
    }
}
