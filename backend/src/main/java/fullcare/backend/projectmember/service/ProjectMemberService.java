package fullcare.backend.projectmember.service;


import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;

    public boolean validateProjectMember(Long projectId, Long memberId) {
        return projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).isPresent();
    }
}
