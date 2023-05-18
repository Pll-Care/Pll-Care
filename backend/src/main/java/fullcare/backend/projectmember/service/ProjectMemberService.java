package fullcare.backend.projectmember.service;


import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;


    // ! 특정 사용자가 프로젝트에 속해있는지 판단하기 위한 메서드 ->
    public boolean validateProjectMember(Long projectId, Long memberId) {
        return projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).isPresent();
    }
}
