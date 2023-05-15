package fullcare.backend.project.repository;

import fullcare.backend.global.State;
import fullcare.backend.project.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("select p from project p join p.projectMembers pm where pm.member.id = :memberId and p.state in :state")
    Page<Project> findProjectList(Pageable pageable, @Param("memberId") Long memberId, @Param("state") List<State> state);


}
