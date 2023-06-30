package fullcare.backend.memo.repository;

import fullcare.backend.member.domain.Member;
import fullcare.backend.memo.domain.BookmarkMemo;
import fullcare.backend.memo.domain.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookmarkMemoRepository extends JpaRepository<BookmarkMemo, Long> {
    Optional<BookmarkMemo> findByMemoAndMember(Memo memo, Member member);

    @Query(value = "select bmm from BookmarkMemo bmm join fetch bmm.memo mm join fetch mm.author where bmm.member.id = :memberId and mm.project.id = :projectId",
            countQuery = "select count(bmm) from BookmarkMemo bmm join bmm.memo mm where bmm.member.id = :memberId and mm.project.id = :projectId")
    Page<BookmarkMemo> findList(Pageable pageable, @Param("projectId") Long projectId, @Param("memberId") Long memberId);

}
