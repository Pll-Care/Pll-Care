package fullcare.backend.projectmember.repository;

import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    boolean existsByProjectIdAndMemberId(Long projectId, Long memberId);

    Optional<ProjectMember> findByProjectIdAndMemberId(Long projectId, Long memberId);

    //@Query("select pm from project_member pm join fetch pm.member where pm.project.id = :projectId")
    @Query("select pm from project_member pm where pm.project.id = :projectId and pm.projectMemberRole.role != :projectMemberRoleType")

    List<ProjectMember> findByProjectId(@Param("projectId") Long projectId, ProjectMemberRoleType projectMemberRoleType);

    @Query(value = "select pm from project_member pm join fetch pm.project where pm.member.id = :memberId",
            countQuery = "select count(pm) from project_member pm where pm.member.id = :memberId")
    Page<ProjectMember> findByMemberId(Pageable pageable, Long memberId);

    void deleteByProjectIdAndMemberId(Long projectId, Long memberId);
    @Query("select pm from project_member pm where pm.project.id = :projectId and pm.projectMemberRole.role = :projectMemberRoleType")
    List<ProjectMember> findApplyListByProjectIdAndProjectMemberRole(Long projectId, ProjectMemberRoleType projectMemberRoleType);
}
