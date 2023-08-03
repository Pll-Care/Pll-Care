package fullcare.backend.projectmember.service;


import fullcare.backend.apply.domain.Apply;
import fullcare.backend.apply.repository.ApplyRepository;
import fullcare.backend.global.State;
import fullcare.backend.global.errorcode.PostErrorCode;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.CompletedProjectException;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.global.exceptionhandling.exception.UnauthorizedAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.post.domain.Post;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.request.*;
import fullcare.backend.project.dto.response.ApplyMemberListResponse;
import fullcare.backend.project.dto.response.ProjectCompleteResponse;
import fullcare.backend.project.dto.response.ProjectLeaderResponse;
import fullcare.backend.project.dto.response.ProjectMemberListResponse;
import fullcare.backend.project.service.ProjectService;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import fullcare.backend.projectmember.domain.ProjectMemberType;
import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import fullcare.backend.recruitment.domain.Recruitment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ApplyRepository applyRepository;

    private final ProjectService projectService;

    public ProjectMember findProjectMember(Long projectId, Long memberId) {
        return projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
    }

    public List<ProjectMemberListResponse> findProjectMembers(Long projectId, Long memberId) {
        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, memberId, true);

        List<ProjectMemberRoleType> projectMemberRoleTypes = new ArrayList<>();
        projectMemberRoleTypes.add(ProjectMemberRoleType.리더);
        projectMemberRoleTypes.add(ProjectMemberRoleType.팀원);

        List<ProjectMember> projectMemberList = projectMemberRepository.findProjectMemberWithMemberByProjectIdAndProjectMemberRole(findProjectMember.getProject().getId(), projectMemberRoleTypes);
        List<ProjectMemberListResponse> response = projectMemberList.stream().map(pm -> ProjectMemberListResponse.builder()
                .id(pm.getMember().getId())
                .name(pm.getMember().getNickname())
                .imageUrl(pm.getMember().getImageUrl())
                .position(pm.getProjectMemberType().getPosition())
                .isLeader(pm.isLeader())
                .build()).collect(Collectors.toList());

        return response;
    }

    public List<ApplyMemberListResponse> findApplyMembers(Long projectId, Long memberId) {

        try {
            ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, memberId, false);

            List<Apply> applyList = applyRepository.findByProjectId(findProjectMember.getProject().getId());
            List<ApplyMemberListResponse> response = applyList.stream().map(a -> ApplyMemberListResponse.builder()
                    .postId(a.getPost().getId())
                    .memberId(a.getMember().getId())
                    .name(a.getMember().getName())
                    .imageUrl(a.getMember().getImageUrl())
                    .position(a.getPosition())
                    .build()).collect(Collectors.toList());

            return response;

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(ProjectErrorCode.INVALID_ACTION);
        }


    }

    @Transactional
    public void updateProjectMemberRole(Long projectId, Long memberId, ProjectMemberRoleType role) {
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
        projectMember.updateRole(role);
    }

    @Transactional
    public void updateProjectMemberPosition(Long projectId, Long memberId, ProjectMemberPositionUpdateRequest request) {
        try {
            ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, memberId, false);

            // ! 프로젝트에 소속되어있지만 리더가 아니라서 멤버 역할 수정 권한이 없음 -> 403 Forbidden
            if (!(findProjectMember.isLeader())) {
                throw new UnauthorizedAccessException(ProjectErrorCode.UNAUTHORIZED_ACTION);
            }

            ProjectMember changeProjectMember = projectMemberRepository.findByProjectIdAndMemberId(findProjectMember.getProject().getId(), request.getMemberId()).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
            changeProjectMember.updatePosition(request.getPosition());

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(ProjectErrorCode.INVALID_ACTION);
        }
    }


    @Transactional
    public void changeProjectLeader(Long projectId, Long memberId, ProjectLeaderChangeRequest request) {

        try {
            ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, memberId, false);

            if (!(findProjectMember.isLeader())) {
                throw new UnauthorizedAccessException(ProjectErrorCode.UNAUTHORIZED_ACTION);
            }

            findProjectMember.updateRole(ProjectMemberRoleType.팀원);
            updateProjectMemberRole(projectId, request.getMemberId(), ProjectMemberRoleType.리더);

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(ProjectErrorCode.INVALID_ACTION);
        }
    }

    public void selfOutProjectMember(Long projectId, Long memberId) {
        try {
            ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, memberId, false);

            // ! 리더는 스스로 탈퇴가 불가능함. 누군가에게 리더를 위임해야한다.
            if (findProjectMember.isLeader()) {
                throw new UnauthorizedAccessException(ProjectErrorCode.LEADER_INVALID_ACTION);
            }

            projectMemberRepository.delete(findProjectMember);

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(ProjectErrorCode.INVALID_ACTION);
        }

    }

    public void kickOutProjectMember(Long projectId, Long memberId, ProjectMemberDeleteRequest request) {
        try {
            ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, memberId, false);

            // ! 프로젝트에 소속되어있지만 리더가 아니라서 강퇴 권한이 없음 -> 403 Forbidden
            if (!(findProjectMember.isLeader())) {
                throw new UnauthorizedAccessException(ProjectErrorCode.UNAUTHORIZED_ACTION);
            } else {
                if (findProjectMember.getMember().getId() == request.getMemberId()) {
                    throw new UnauthorizedAccessException(ProjectErrorCode.LEADER_INVALID_ACTION);
                }
            }

            projectMemberRepository.deleteByProjectIdAndMemberId(findProjectMember.getProject().getId(), request.getMemberId());

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(ProjectErrorCode.INVALID_ACTION);
        }

    }

    @Transactional
    public void acceptApplyMember(Long projectId, Long memberId, ApplyMemberAcceptRequest request) {

        try {
            ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, memberId, false);

            // ! 프로젝트에 소속되어있지만 리더가 아니라서 지원자 수락 권한이 없음 -> 403 Forbidden
            if (!(findProjectMember.isLeader())) {
                throw new UnauthorizedAccessException(ProjectErrorCode.UNAUTHORIZED_ACTION);
            }

            // 쿼리1
            Apply findApply = applyRepository.findByPostIdAndMemberId(request.getPostId(), memberId).orElseThrow(() -> new EntityNotFoundException(PostErrorCode.APPLY_NOT_FOUND)); // todo 지원자 정보가 없습니다.

            // 쿼리2
            Member findMember = findApply.getMember();

            // 쿼리3
            Post findPost = findApply.getPost();

            // 쿼리4
            Project findProject = findPost.getProject();

            // 쿼리5  -> o
            List<Recruitment> recruitments = findPost.getRecruitments();

            // 쿼리6
            Recruitment recruitment = recruitments.stream().filter(r -> r.getRecruitPosition() == findApply.getPosition()).findAny().orElseThrow();
            recruitment.updateRecruitment(); // -> o

            // 쿼리7 -> o
            findProject.addMember(findMember, new ProjectMemberType(ProjectMemberRoleType.팀원, findApply.getPosition()));

            // 쿼리8 -> o
            applyRepository.delete(findApply);

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(ProjectErrorCode.INVALID_ACTION);
        }


    }

    @Transactional
    public void rejectApplyMember(Long projectId, Long memberId, ApplyMemberRejectRequest request) {

        try {
            ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, memberId, false);

            // ! 프로젝트에 소속되어있지만 리더가 아니라서 지원자 거절 권한이 없음 -> 403 Forbidden
            if (!(findProjectMember.isLeader())) {
                throw new UnauthorizedAccessException(ProjectErrorCode.UNAUTHORIZED_ACTION);
            }

            Apply findApply = applyRepository.findByPostIdAndMemberId(request.getPostId(), request.getMemberId()).orElseThrow(() -> new EntityNotFoundException(PostErrorCode.APPLY_NOT_FOUND)); // todo 지원자 정보가 없습니다.

            // ? state만 지원 거절로 바꾸고, 사용자가 confirm 하면 삭제하는 방향?
            applyRepository.delete(findApply);

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(ProjectErrorCode.INVALID_ACTION);
        }
    }

    public ProjectCompleteResponse checkProjectComplete(Long projectId, Long memberId) {

        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, memberId, true);
        Project findProject = findProjectMember.getProject();

        ProjectCompleteResponse response = ProjectCompleteResponse.builder()
                .isCompleted(findProject.getState() == State.COMPLETE)
                .build();

        return response;

    }

    public ProjectLeaderResponse checkProjectLeader(Long projectId, Long memberId) {
        try {
            ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, memberId, false);

            ProjectLeaderResponse response = ProjectLeaderResponse.builder()
                    .isLeader(findProjectMember.isLeader())
                    .build();

            return response;

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(ProjectErrorCode.INVALID_ACTION);
        }
    }


}
