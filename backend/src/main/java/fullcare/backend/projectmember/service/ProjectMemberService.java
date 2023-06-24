package fullcare.backend.projectmember.service;


import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.domain.ProjectMemberRole;
import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public boolean validateProjectMember(Long projectId, Long memberId) {
        return projectMemberRepository.existsByProjectIdAndMemberId(projectId, memberId);
    }

    public ProjectMember findProjectMember(Long projectId, Long memberId) {
        return projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트 멤버가 존재하지 않습니다."));

    }

    public void addProjectMember(Long projectId, Long memberId, ProjectMemberRole role) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트가 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));

        project.addMember(member, role);
    }
    public void updateProjectMemberRole(Long pmId, ProjectMemberRole projectMemberRole){
        ProjectMember projectMember = projectMemberRepository.findById(pmId).orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));
        projectMember.updateRole(projectMemberRole);

    }
    public void deleteProjectMember(Long projectId, Long memberId) {
        projectMemberRepository.deleteByProjectIdAndMemberId(projectId, memberId);
    }
}
