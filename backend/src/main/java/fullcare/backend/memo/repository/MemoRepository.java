package fullcare.backend.memo.repository;

import fullcare.backend.memo.domain.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    @Query("select m from Memo m join m.project p on p.id = :projectId")
    Page<Memo> findList(@Param("projectId") Long projectId, Pageable pageable);

}
