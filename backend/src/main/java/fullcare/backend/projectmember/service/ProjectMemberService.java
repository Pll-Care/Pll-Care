package fullcare.backend.projectmember.service;


import fullcare.backend.apply.domain.Apply;
import fullcare.backend.apply.repository.ApplyRepository;
import fullcare.backend.global.errorcode.PostErrorCode;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.post.domain.Post;
import fullcare.backend.post.domain.Recruitment;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.response.ApplyMemberListResponse;
import fullcare.backend.project.dto.response.ProjectMemberListResponse;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.domain.ProjectMemberPositionType;
import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import fullcare.backend.projectmember.domain.ProjectMemberType;
import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ApplyRepository applyRepository;

    public ProjectMember findProjectMember(Long projectId, Long memberId) {
        return projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
    }

    public List<ProjectMemberListResponse> findProjectMembers(Long projectId) {
        List<ProjectMemberRoleType> projectMemberRoleTypes = new ArrayList<>();
        projectMemberRoleTypes.add(ProjectMemberRoleType.리더);
        projectMemberRoleTypes.add(ProjectMemberRoleType.팀원);

        List<ProjectMember> projectMemberList = projectMemberRepository.findProjectMemberWithMemberByProjectIdAndProjectMemberRole(projectId, projectMemberRoleTypes);
        List<ProjectMemberListResponse> response = projectMemberList.stream().map(pm -> ProjectMemberListResponse.builder()
                .id(pm.getMember().getId())
                .name(pm.getMember().getNickname())
                .imageUrl(pm.getMember().getImageUrl())
                .position(pm.getProjectMemberType().getPosition())
                .isLeader(pm.isLeader())
                .build()).collect(Collectors.toList());

        return response;
    }

    public Optional<Apply> findApplyMember(Long postId, Long memberId) {
        return applyRepository.findByPostIdAndMemberId(postId, memberId);
    }

    public List<ApplyMemberListResponse> findApplyMembers(Long projectId) {
        List<Apply> applyList = applyRepository.findByProjectId(projectId);
        List<ApplyMemberListResponse> response = applyList.stream().map(a -> ApplyMemberListResponse.builder()
                .postId(a.getPost().getId())
                .memberId(a.getMember().getId())
                .name(a.getMember().getName())
                .imageUrl(a.getMember().getImageUrl())
                .position(a.getPosition())
                .build()).collect(Collectors.toList());

        return response;
    }

    @Transactional
    public void updateProjectMemberRole(Long projectId, Long memberId, ProjectMemberRoleType role) {
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
        projectMember.updateRole(role);
    }

    @Transactional
    public void updateProjectMemberPosition(Long projectId, Long memberId, ProjectMemberPositionType position) {
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
        projectMember.updatePosition(position);
    }

    @Transactional
    public void deleteProjectMember(Long projectId, Long memberId) {
        projectMemberRepository.deleteByProjectIdAndMemberId(projectId, memberId);
    }

    @Transactional
    public void changeProjectLeader(Long projectId, Long newLeaderId, Long oldLeaderId) {
        updateProjectMemberRole(projectId, oldLeaderId, ProjectMemberRoleType.팀원);
        updateProjectMemberRole(projectId, newLeaderId, ProjectMemberRoleType.리더);
    }

    @Transactional
    public void acceptApplyMember(Long postId, Long memberId) {
        // 쿼리1
        Apply findApply = applyRepository.findByPostIdAndMemberId(postId, memberId).orElseThrow(() -> new EntityNotFoundException(PostErrorCode.APPLY_NOT_FOUND)); // todo 지원자 정보가 없습니다.

        // 쿼리2
        Member findMember = findApply.getMember();

        // 쿼리3
        Post findPost = findApply.getPost();

        // 쿼리4
        Project findProject = findPost.getProject();

        // 쿼리5
        List<Recruitment> recruitments = findPost.getRecruitments();

        // 쿼리6
        Recruitment recruitment = recruitments.stream().filter(r -> r.getRecruitPosition() == findApply.getPosition()).findAny().orElseThrow();
        recruitment.updateRecruitment();

        // 쿼리7
        findProject.addMember(findMember, new ProjectMemberType(ProjectMemberRoleType.팀원, findApply.getPosition()));
    }

    @Transactional
    public void rejectApplyMember(Long postId, Long memberId) {
        Apply findApply = applyRepository.findByPostIdAndMemberId(postId, memberId).orElseThrow(() -> new EntityNotFoundException(PostErrorCode.APPLY_NOT_FOUND)); // todo 지원자 정보가 없습니다.

        // ? state만 지원 거절로 바꾸고, 사용자가 confirm 하면 삭제하는 방향?
        applyRepository.delete(findApply);
    }


}
