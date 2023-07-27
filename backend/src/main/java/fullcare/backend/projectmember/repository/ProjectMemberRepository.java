package fullcare.backend.projectmember.repository;

import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {


    @EntityGraph(attributePaths = {"project"})
    Optional<ProjectMember> findPMWithProjectByProjectIdAndMemberId(Long projectId, Long memberId);

    Optional<ProjectMember> findByProjectIdAndMemberId(Long projectId, Long memberId);

    @EntityGraph(attributePaths = {"project"})
    Page<ProjectMember> findByMemberId(Pageable pageable, Long memberId);

    void deleteByProjectIdAndMemberId(Long projectId, Long memberId);

    @Query("select pm from project_member pm where pm.project.id = :projectId and pm.projectMemberType.role != :projectMemberRoleType")
    List<ProjectMember> findByProjectIdAndProjectMemberRole(@Param("projectId") Long projectId, @Param("projectMemberRoleType") ProjectMemberRoleType projectMemberRoleType);


    @EntityGraph(attributePaths = {"member"})
    @Query("select pm from project_member pm where pm.project.id = :projectId and pm.projectMemberType.role in :projectMemberRoleTypes")
    List<ProjectMember> findProjectMemberWithMemberByProjectIdAndProjectMemberRole(@Param("projectId") Long projectId, @Param("projectMemberRoleTypes") List<ProjectMemberRoleType> projectMemberRoleTypes);

}
