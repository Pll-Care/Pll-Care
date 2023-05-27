package fullcare.backend.projectmember.repository;

import fullcare.backend.projectmember.domain.ProjectMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    @Query("select pm from project_member pm where pm.project.id = :projectId and pm.member.id = :memberId")
    Optional<ProjectMember> findByProjectIdAndMemberId(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

    //@Query("select pm from project_member pm join fetch pm.member where pm.project.id = :projectId")
    List<ProjectMember> findByProjectId(@Param("projectId") Long projectId);
    @Query(value = "select pm from project_member pm join fetch pm.project where pm.member.id = :memberId",
            countQuery = "select count(pm) from project_member pm where pm.member.id = :memberId")
    Page<ProjectMember> findByMemberId(Pageable pageable, Long memberId);
}
