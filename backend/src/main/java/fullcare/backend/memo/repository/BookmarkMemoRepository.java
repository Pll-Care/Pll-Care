package fullcare.backend.memo.repository;

import fullcare.backend.member.domain.Member;
import fullcare.backend.memo.domain.BookmarkMemo;
import fullcare.backend.memo.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkMemoRepository extends JpaRepository<BookmarkMemo, Long> {
    Optional<BookmarkMemo> findByMemberAndMemo(Member member, Memo memo);

    // ! 페이징 기준 -> 북마크한 날짜 or 회의록이 생성된 날짜
    // * 페치조인에 페이징 안먹힘
    // ? DB 상으로 페이징 쿼리가 안나가는게 어떻게 페이징?
    //    @Query("select bmm from BookmarkMemo bmm join fetch bmm.memo mm where bmm.member.id = :memberId and mm.project.id = :projectId ")
    //    @Query("select bmm from BookmarkMemo bmm join fetch bmm.memo where bmm.member.id = :memberId and bmm.memo.project.id = :projectId")
    @Query("select bmm from BookmarkMemo bmm join fetch bmm.memo mm join mm.project p where bmm.member.id = :memberId and p.id = :projectId")
    List<BookmarkMemo> findList(@Param("projectId") Long projectId, @Param("memberId") Long memberId);


    @Query("select bmm from BookmarkMemo bmm join fetch bmm.memo where bmm.member.id = :memberId and bmm.memo.id = :memoId")
    Optional<BookmarkMemo> findByMemberIdAndMemoId(@Param("memberId") Long memberId, @Param("memoId") Long memoId);
}
