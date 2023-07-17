package fullcare.backend.projectmember.service;


import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.response.ProjectMemberListResponse;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.domain.ProjectMemberRole;
import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;


    public boolean validateProjectMember(Long projectId, Long memberId) {
        return projectMemberRepository.existsByProjectIdAndMemberId(projectId, memberId);
    }

    public Optional<ProjectMember> findProjectMemberOptional(Long projectId, Long memberId) {
        return projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId);
    }

    public ProjectMember findProjectMember(Long projectId, Long memberId) {
        return projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
    }

    public void addProjectMember(Long projectId, Member member, ProjectMemberRole role) {
        Project project = projectRepository.findJoinPMJoinMemberById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
        project.addMember(member, role);
    }

    public void updateProjectMemberRole(Long projectId, Long memberId, ProjectMemberRole projectMemberRole) {
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
        projectMember.updateRole(projectMemberRole);
    }

    public void deleteProjectMember(Long projectId, Long memberId) {
        projectMemberRepository.deleteByProjectIdAndMemberId(projectId, memberId);
    }

    public void changeProjectLeader(Long projectId, Long newLeaderId, Long oldLeaderId) {
        ProjectMember oldLeader = projectMemberRepository.findByProjectIdAndMemberId(projectId, oldLeaderId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
        ProjectMember newLeader = projectMemberRepository.findByProjectIdAndMemberId(projectId, newLeaderId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));

        oldLeader.updateRole(new ProjectMemberRole(ProjectMemberRoleType.팀원, oldLeader.getProjectMemberRole().getPosition()));
        newLeader.updateRole(new ProjectMemberRole(ProjectMemberRoleType.리더, newLeader.getProjectMemberRole().getPosition()));
    }

    public List<ProjectMemberListResponse> findProjectMembers(Long projectId) {
        List<ProjectMember> pmList = projectMemberRepository.findByProjectIdAndProjectMemberRole(projectId, ProjectMemberRoleType.미정);

        List<ProjectMemberListResponse> response = pmList.stream().map(pm -> ProjectMemberListResponse.builder()
                .id(pm.getMember().getId())
                .name(pm.getMember().getNickname())
                .imageUrl(pm.getMember().getImageUrl())
                .position(pm.getProjectMemberRole().getPosition())
                .isLeader(pm.isLeader())
                .build()).collect(Collectors.toList());

        return response;

    }

    public List<ProjectMemberListResponse> findApplyList(Long projectId) {
        List<ProjectMember> pmList = projectMemberRepository.findApplyListByProjectIdAndProjectMemberRole(projectId, ProjectMemberRoleType.미정);
        List<ProjectMemberListResponse> response = pmList.stream().map(pms -> ProjectMemberListResponse.builder()
                .id(pms.getMember().getId())
                .name(pms.getMember().getName())
                .imageUrl(pms.getMember().getImageUrl())
                .position(pms.getProjectMemberRole().getPosition())
                .isLeader(false)
                .build()).collect(Collectors.toList());
        return response;
    }


}
