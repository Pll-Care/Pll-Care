package fullcare.backend.project.repository;

import fullcare.backend.global.State;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.response.ProjectSimpleListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(value = "select p from project p join p.projectMembers pm where pm.member.id = :memberId and p.state in :state",
            countQuery = "select count(p) from project p join p.projectMembers pm where pm.member.id = :memberId and p.state in :state")
    Page<Project> findProjectList(Pageable pageable, @Param("memberId") Long memberId, @Param("state") List<State> state);

    @Query(value = "select new fullcare.backend.project.dto.response.ProjectSimpleListResponse(p.id, p.title) from project p join p.projectMembers pm where pm.member.id = :memberId and p.state = 'ONGOING'")
    List<ProjectSimpleListResponse> findSimpleProjectList(@Param("memberId") Long memberId);

    @Query("select p from project p join fetch p.projectMembers pm join fetch pm.member where p.id = :projectId")
    Optional<Project> findJoinPMJoinMemberById(@Param("projectId") Long projectId);

}
