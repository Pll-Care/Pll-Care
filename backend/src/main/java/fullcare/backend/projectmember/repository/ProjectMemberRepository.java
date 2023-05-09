package fullcare.backend.projectmember.repository;

import fullcare.backend.projectmember.domain.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    @Query("select pm from project_member pm where pm.member.id= :memberId and pm.project.id= :projectId")
    Optional<ProjectMember> findByProjectIdAndMemberId(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

}
