package fullcare.backend.project.service;

import fullcare.backend.global.State;
import fullcare.backend.global.errorcode.MemberErrorCode;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.CompletedProjectException;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.global.exceptionhandling.exception.InvalidDateRangeException;
import fullcare.backend.global.exceptionhandling.exception.UnauthorizedAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.request.ProjectCreateRequest;
import fullcare.backend.project.dto.request.ProjectUpdateRequest;
import fullcare.backend.project.dto.response.ProjectDetailResponse;
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
    private final MemberRepository memberRepository;

    private final S3Service s3Service;


    // ! 꼭 Project 클래스타입으로 반환해야하는가?(테스트 데이터 생성을 위해 클래스 타입으로 반환중)
    @Transactional
    public Project createProject(Long memberId, ProjectCreateRequest request) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));

        Project newProject = Project.createNewProject()
                .title(request.getTitle())
                .description(request.getDescription())
                .state(State.ONGOING)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .imageUrl(request.getImageUrl())
                .build();

        newProject.addMember(findMember, new ProjectMemberType(ProjectMemberRoleType.리더, ProjectMemberPositionType.미정));
        return projectRepository.save(newProject);
    }

    @Transactional
    public void updateProject(Long projectId, Long memberId, ProjectUpdateRequest request) {

        try {
            ProjectMember findProjectMember = isProjectAvailable(projectId, memberId, false);

            // ! 프로젝트에 소속되어있지만 리더가 아니라서 수정 권한이 없음 -> 403 Forbidden
            if (!(findProjectMember.isLeader())) {
                throw new UnauthorizedAccessException(ProjectErrorCode.UNAUTHORIZED_MODIFY);
            }

            if (!request.getStartDate().isBefore(request.getEndDate())) {
                throw new InvalidDateRangeException(ProjectErrorCode.INVALID_DATE_RANGE);
            }

            Project findProject = findProjectMember.getProject();
            s3Service.delete(findProject.getImageUrl());
            findProject.updateAll(request.getTitle(), request.getDescription(), request.getState(),
                    request.getStartDate(), request.getEndDate(), request.getImageUrl());

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(ProjectErrorCode.INVALID_MODIFY);
        }
    }

    @Transactional
    public void deleteProject(Long projectId, Long memberId) {

        try {
            ProjectMember findProjectMember = isProjectAvailable(projectId, memberId, false);

            // ! 프로젝트에 소속되어있지만 리더가 아니라서 삭제 권한이 없음 -> 403 Forbidden
            if (!(findProjectMember.isLeader())) {
                throw new UnauthorizedAccessException(ProjectErrorCode.UNAUTHORIZED_DELETE);
            }

            Project findProject = findProjectMember.getProject();
            if (findProject.getImageUrl() != null) {
                s3Service.delete(findProject.getImageUrl());
            }

            projectRepository.delete(findProject);

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(ProjectErrorCode.INVALID_DELETE);
        }
    }

    @Transactional
    public void completeProject(Long projectId, Long memberId) {

        ProjectMember findProjectMember = isProjectAvailable(projectId, memberId, false);

        // ! 프로젝트에 소속되어있지만 리더가 아니라서 상태 수정 권한이 없음 -> 403 Forbidden
        if (!findProjectMember.isLeader()) {
            throw new UnauthorizedAccessException(ProjectErrorCode.UNAUTHORIZED_COMPLETE);
        }

        Project findProject = findProjectMember.getProject();
        findProject.complete();
    }

    public ProjectDetailResponse findProjectDetail(Long projectId, Long memberId) {

        ProjectMember findProjectMember = isProjectAvailable(projectId, memberId, true);
        Project findProject = findProjectMember.getProject();

        ProjectDetailResponse projectDetailResponse = ProjectDetailResponse.builder()
                .title(findProject.getTitle())
                .description(findProject.getDescription())
                .startDate(findProject.getStartDate())
                .endDate(findProject.getEndDate())
                .imageUrl(findProject.getImageUrl())
                .state(findProject.getState())
                .build();

        return projectDetailResponse;
    }

    public Project findProject(Long projectId) {
        return projectRepository.findProjectWithPMAndMemberById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
    }

    public CustomPageImpl<ProjectListResponse> findProjectList(Pageable pageable, Long memberId, List<State> states) {
        Page<Project> pageProject = projectRepository.findListWithPaging(pageable, memberId, states);

        List<ProjectListResponse> content = pageProject.stream().map(p -> ProjectListResponse.builder()
                .projectId(p.getId())
                .title(p.getTitle())
                .description(p.getDescription())
                .startDate(p.getStartDate())
                .endDate(p.getEndDate())
                .state(p.getState())
                .imageUrl(p.getImageUrl())
                .build()
        ).collect(Collectors.toList());

        return new CustomPageImpl<>(content, pageable, pageProject.getTotalElements());
    }

    public List<ProjectSimpleListResponse> findSimpleProjectList(Long memberId) {
        return projectRepository.findSimpleList(memberId);
    }

    // ! todo readonly 옵션으로 데이터 읽기 용도로만 호출할 경우 프로젝트가 완료되어도 검증 통과
    public ProjectMember isProjectAvailable(Long projectId, Long memberId, boolean readOnlyAfterProjectComplete) {
        // * 프로젝트가 존재하는 검증
        Project findProject = projectRepository.findProjectWithPMAndMemberById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));

        // * 프로젝트에 소속된 사람인지 검증
        // ! 사용자가 프로젝트에 소속되어있다면, 해당 프로젝트 멤버 엔티티를 반환한다.
        // ! 사용자가 프로젝트에 소속되어있지 않다면, 예외를 던진다.
        ProjectMember findProjectMember = findProject.getProjectMembers().stream()
                .filter(pmm -> pmm.getMember().getId().equals(memberId)).findAny()
                .orElseThrow(() -> new UnauthorizedAccessException(ProjectErrorCode.UNAUTHORIZED_ACCESS));

        // * 프로젝트가 완료되었는지 검증
        if (findProject.isCompleted() && !readOnlyAfterProjectComplete) {
            throw new CompletedProjectException(ProjectErrorCode.PROJECT_COMPLETED);
        }

        return findProjectMember;
    }
}
