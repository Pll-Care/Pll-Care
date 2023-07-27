package fullcare.backend.project.service;

import fullcare.backend.global.State;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.CompletedProjectException;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.global.exceptionhandling.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.request.ProjectCreateRequest;
import fullcare.backend.project.dto.request.ProjectUpdateRequest;
import fullcare.backend.project.dto.response.ProjectListResponse;
import fullcare.backend.project.dto.response.ProjectSimpleListResponse;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.domain.ProjectMemberPositionType;
import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import fullcare.backend.projectmember.domain.ProjectMemberType;
import fullcare.backend.s3.S3Service;
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
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final S3Service s3Service;

    @Transactional
    public Project createProject(Member member, ProjectCreateRequest request) {
        Project newProject = Project.createNewProject()
                .title(request.getTitle())
                .description(request.getDescription())
                .state(State.ONGOING)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .imageUrl(request.getImageUrl())
                .build();

        newProject.addMember(member, new ProjectMemberType(ProjectMemberRoleType.리더, ProjectMemberPositionType.미정));
        return projectRepository.save(newProject);
    }

    @Transactional
    public void updateProject(Long projectId, ProjectUpdateRequest request) {
        Project findProject = findSimpleProject(projectId);
        s3Service.delete(findProject.getImageUrl());

        findProject.updateAll(request.getTitle(), request.getDescription(), request.getState(),
                request.getStartDate(), request.getEndDate(), request.getImageUrl());
    }

    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));

        if (project.getImageUrl() != null) {
            s3Service.delete(project.getImageUrl());
        }

        projectRepository.deleteById(projectId);
    }

    @Transactional
    public void completeProject(Long projectId) {
        Project findProject = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
        findProject.complete();
    }

    public Project findProject(Long projectId) {
        return projectRepository.findProjectWithPMAndMemberById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
    }

    public Project findSimpleProject(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
    }

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
        return projectRepository.findSimpleProjectList(memberId);
    }

    // ! todo readonly 옵션으로 데이터 읽기 용도로만 호출할 경우 프로젝트가 완료되어도 검증 통과
    public ProjectMember isProjectAvailable(Long projectId, Long memberId, boolean readOnly) {
        // * 프로젝트가 존재하는 검증
        Project findProject = projectRepository.findProjectWithPMById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));

        // * 프로젝트에 소속된 사람인지 검증
        // ! 사용자가 프로젝트에 소속되어있다면, 해당 프로젝트 멤버 엔티티를 반환한다.
        // ! 사용자가 프로젝트에 소속되어있지 않다면, 예외를 던진다.
        ProjectMember findProjectMember = findProject.getProjectMembers().stream()
                .filter(pmm -> pmm.getMember().getId().equals(memberId)).findAny()
                .orElseThrow(() -> new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS));

        // * 프로젝트가 완료되었는지 검증
        if (findProject.isCompleted() && !readOnly) {
            throw new CompletedProjectException(ProjectErrorCode.PROJECT_COMPLETED);
        }

        return findProjectMember;
    }
}
