package fullcare.backend.projectmember.service;


import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;

    public boolean validateProjectMember(Long projectId, Long memberId) {
        return projectMemberRepository.existsByProjectIdAndMemberId(projectId, memberId);
    }

    public ProjectMember findProjectMember(Long projectId, Long memberId) {
        return projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트 멤버가 존재하지 않습니다."));

    }
}
