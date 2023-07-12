package fullcare.backend.project.service;

import fullcare.backend.evaluation.domain.FinalTermEvaluation;
import fullcare.backend.global.State;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.request.ProjectCreateRequest;
import fullcare.backend.project.dto.request.ProjectUpdateRequest;
import fullcare.backend.project.dto.response.ProjectListResponse;
import fullcare.backend.project.dto.response.ProjectSimpleListResponse;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.projectmember.domain.ProjectMemberRole;
import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import fullcare.backend.s3.UploadService;
import fullcare.backend.util.CustomPageImpl;
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
    private final UploadService uploadService;


    public Project createProject(Member member, ProjectCreateRequest request) {
        Project newProject = Project.createNewProject()
                .title(request.getTitle())
                .description(request.getDescription())
                .state(State.ONGOING)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .imageUrl(request.getImageUrl())
                .build();

        newProject.addMember(member, new ProjectMemberRole(ProjectMemberRoleType.리더, ProjectMemberRoleType.미정));
        return projectRepository.save(newProject);
    }

    public void updateProject(Long projectId, ProjectUpdateRequest projectUpdateRequest) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
        String imageUrl = project.getImageUrl();
        project.update(projectUpdateRequest);
        uploadService.delete(imageUrl);
    }

    public Project findProject(Long projectId) {
        return projectRepository.findJoinPMJoinMemberById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
    }

    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));


        // ? 무슨 용도의 코드인가
        List<FinalTermEvaluation> finalTermEvaluations = project.getFinalTermEvaluations();
        for (FinalTermEvaluation fe : finalTermEvaluations) {
            fe.setProjectNull();
        }


        projectRepository.deleteById(projectId);
        if (project.getImageUrl() != null) {
            uploadService.delete(project.getImageUrl());
        }
    }

    public void completeProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
        project.complete();
    }

    @Transactional(readOnly = true)
    public CustomPageImpl<ProjectListResponse> findProjectList(Pageable pageable, Long memberId, List<State> states) {
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

    public List<ProjectSimpleListResponse> findSimpleProjectList(Long memberId) {
        List<ProjectSimpleListResponse> simpleProjectList = projectRepository.findSimpleProjectList(memberId);
        
        return simpleProjectList;
    }
}
