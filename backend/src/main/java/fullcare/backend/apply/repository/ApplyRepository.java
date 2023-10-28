package fullcare.backend.apply.repository;

import fullcare.backend.apply.domain.Apply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
    Optional<Apply> findByPostIdAndMemberId(Long postId, Long memberId);

    @Query("select a from Apply a join a.post p where p.author.project.id = :projectId")
    @EntityGraph(attributePaths = {"member"})
    List<Apply> findByProjectId(@Param("projectId") Long projectId);

    @EntityGraph(attributePaths = {"member", "post"})
    Page<Apply> findByMemberId(Long memberId, Pageable pageable);
}
