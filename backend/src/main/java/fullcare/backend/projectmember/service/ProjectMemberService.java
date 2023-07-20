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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;


    public boolean validateProjectMember(Long projectId, Long memberId) {
        projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND)); // ! 프로젝트가 존재하는지 검증
        return projectMemberRepository.existsByProjectIdAndMemberId(projectId, memberId); // ! 프로젝트에 소속된 사용자인지 검증
    }


    public ProjectMember findProjectMember(Long projectId, Long memberId) {
        return projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
    }

    @Transactional
    public void addProjectMember(Long projectId, Member member, ProjectMemberRole role) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
        project.addMember(member, role);
    }

    @Transactional
    public void updateProjectMemberRole(Long projectId, Long memberId, ProjectMemberRole projectMemberRole) {
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
        projectMember.updateRole(projectMemberRole);
    }

    @Transactional
    public void deleteProjectMember(Long projectId, Long memberId) {
        projectMemberRepository.deleteByProjectIdAndMemberId(projectId, memberId);
    }

    @Transactional
    public void changeProjectLeader(Long projectId, Long newLeaderId, Long oldLeaderId) {
        ProjectMember oldLeader = projectMemberRepository.findByProjectIdAndMemberId(projectId, oldLeaderId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
        ProjectMember newLeader = projectMemberRepository.findByProjectIdAndMemberId(projectId, newLeaderId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));

        oldLeader.updateRole(new ProjectMemberRole(ProjectMemberRoleType.팀원, oldLeader.getProjectMemberRole().getPosition()));
        newLeader.updateRole(new ProjectMemberRole(ProjectMemberRoleType.리더, newLeader.getProjectMemberRole().getPosition()));
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
                .position(pm.getProjectMemberRole().getPosition())
                .isLeader(pm.isLeader())
                .build()).collect(Collectors.toList());

        return response;
    }

    public List<ProjectMemberListResponse> findApplyList(Long projectId) {
        List<ProjectMemberRoleType> projectMemberRoleTypes = new ArrayList<>();
        projectMemberRoleTypes.add(ProjectMemberRoleType.미정);

        List<ProjectMember> projectMemberList = projectMemberRepository.findProjectMemberWithMemberByProjectIdAndProjectMemberRole(projectId, projectMemberRoleTypes);

        List<ProjectMemberListResponse> response = projectMemberList.stream().map(pm -> ProjectMemberListResponse.builder()
                .id(pm.getMember().getId())
                .name(pm.getMember().getName())
                .imageUrl(pm.getMember().getImageUrl())
                .position(pm.getProjectMemberRole().getPosition())
                .isLeader(false)
                .build()).collect(Collectors.toList());

        return response;
    }
}
