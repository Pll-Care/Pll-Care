package fullcare.backend.memo.repository;

import fullcare.backend.memo.domain.Memo;
import fullcare.backend.project.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    @Query("select m from Memo m join m.project p on p.id = :projectId")
    Page<Memo> findList(Pageable pageable, @Param("projectId") Long projectId);

    List<Memo> findTop5ByProjectOrderByCreatedDateDesc(Project project);

    @Query("select m from Memo m join m.bookmark bm on bm.member.id = :memberId where m.project.id = :projectId")
    Page<Memo> findBookmarkList(Pageable pageable, @Param("projectId") Long projectId, @Param("memberId") Long memberId);
}
