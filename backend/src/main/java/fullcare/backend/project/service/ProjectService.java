package fullcare.backend.project.service;

import fullcare.backend.evaluation.domain.FinalTermEvaluation;
import fullcare.backend.global.State;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.request.ProjectCreateRequest;
import fullcare.backend.project.dto.request.ProjectStateUpdateRequest;
import fullcare.backend.project.dto.request.ProjectUpdateRequest;
import fullcare.backend.project.dto.response.ProjectListResponse;
import fullcare.backend.project.dto.response.ProjectMemberListResponse;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.domain.ProjectMemberRole;
import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import fullcare.backend.s3.UploadService;
import fullcare.backend.util.CustomPageImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    private final ProjectMemberRepository projectMemberRepository;
    private final UploadService uploadService;

//    public static List<ProjectMemberListResponse> getProjectMemberListResponses(Long projectId, ProjectMemberRepository projectMemberRepository) {
//        List<ProjectMember> pmList = projectMemberRepository.findByProjectId(projectId, ProjectMemberRoleType.미정);
//        if (pmList.size() == 0) {
//            throw new RuntimeException("프로젝트 조회 불가");
//        }
//        List<ProjectMemberListResponse> response = pmList.stream().map(pms -> ProjectMemberListResponse.builder()
//                .id(pms.getMember().getId())
//                .name(pms.getMember().getName()).build()).collect(Collectors.toList());
//        return response;
//    }

    @Transactional(readOnly = true)
    public CustomPageImpl<ProjectListResponse> findMyProjectList(Pageable pageable, Long memberId, List<State> states) {
        Page<Project> pageProject = projectRepository.findProjectList(pageable, memberId, states);

        List<ProjectListResponse> content = pageProject.stream().map(p -> ProjectListResponse.builder()
                .projectId(p.getId())
                .title(p.getTitle())
                .description(p.getDescription().length() > 20 ? p.getDescription().substring(0, 20) + "..." : p.getDescription())
                .startDate(p.getStartDate())
                .endDate(p.getEndDate())
                .state(p.getState())
                .imageUrl(p.getImageUrl())
                .build()
        ).collect(Collectors.toList());

        return new CustomPageImpl<>(content, pageable, pageProject.getTotalElements());
    }

    public Project createProject(Long memberId, ProjectCreateRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Project newProject = Project.createNewProject()
                .title(request.getTitle())
                .description(request.getDescription())
                .state(State.ONGOING)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .imageUrl(request.getImageUrl())
                .build();

        newProject.addMember(member, new ProjectMemberRole(ProjectMemberRoleType.리더, ProjectMemberRoleType.미정));

        Project project = projectRepository.save(newProject);

        return project;
    }

    public Project findProject(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("프로젝트 정보가 없습니다."));
    }

    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("프로젝트 정보가 없습니다."));
        List<FinalTermEvaluation> finalTermEvaluations = project.getFinalTermEvaluations();
        for (FinalTermEvaluation fe : finalTermEvaluations) {
            fe.setProjectNull();
        }
        projectRepository.deleteById(projectId);
        if (project.getImageUrl() != null){
            uploadService.delete(project.getImageUrl());
        }
    }


    public List<ProjectMemberListResponse> findProjectMembers(Long projectId) {
        return getProjectMemberListResponses(projectId, projectMemberRepository);

    }

    public List<ProjectMemberListResponse> findApplyList(Long projectId){
        List<ProjectMember> pmList = projectMemberRepository.findApplyListByProjectIdAndProjectMemberRole(projectId, ProjectMemberRoleType.미정);
        List<ProjectMemberListResponse> response = pmList.stream().map(pms -> ProjectMemberListResponse.builder()
                .id(pms.getMember().getId())
                .pmId(pms.getId())
                .name(pms.getMember().getName())
                .imageUrl(pms.getMember().getImageUrl())
                .position(pms.getProjectMemberRole().getPosition().toString())
                .build()).collect(Collectors.toList());
        return response;
    }
    public static List<ProjectMemberListResponse> getProjectMemberListResponses(Long projectId, ProjectMemberRepository projectMemberRepository) {
        List<ProjectMember> pmList = projectMemberRepository.findByProjectId(projectId, ProjectMemberRoleType.미정);
        if (pmList.size()==0){
            throw new RuntimeException("프로젝트 조회 불가");
        }
        List<ProjectMemberListResponse> response = pmList.stream().map(pms -> ProjectMemberListResponse.builder()
                .id(pms.getMember().getId())
                .pmId(pms.getId())
                .name(pms.getMember().getName())
                .imageUrl(pms.getMember().getImageUrl())
                .position(pms.getProjectMemberRole().getPosition().toString())
                .build()).collect(Collectors.toList());
        return response;
    }

    public void updateState(Long projectId, ProjectStateUpdateRequest projectStateUpdateRequest) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        project.updateState(projectStateUpdateRequest.getState());
    }

    public void update(Long projectId, ProjectUpdateRequest projectUpdateRequest) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        String imageUrl = project.getImageUrl();
        project.update(projectUpdateRequest);
        uploadService.delete(imageUrl);
    }
}
