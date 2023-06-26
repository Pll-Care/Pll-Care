package fullcare.backend.memo.repository;

import fullcare.backend.member.domain.Member;
import fullcare.backend.memo.domain.BookmarkMemo;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.memo.dto.response.BookmarkMemoListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookmarkMemoRepository extends JpaRepository<BookmarkMemo, Long> {
    Optional<BookmarkMemo> findByMemoAndMember(Memo memo, Member member);

    @Query(value = "select bmm from BookmarkMemo bmm join fetch bmm.memo mm where bmm.member.id = :memberId and mm.project.id = :projectId",
            countQuery = "select count(bmm) from BookmarkMemo bmm join bmm.memo mm where bmm.member.id = :memberId and mm.project.id = :projectId")
    Page<BookmarkMemo> findList(Pageable pageable, @Param("projectId") Long projectId, @Param("memberId") Long memberId);

    @Query(value = "select new fullcare.backend.memo.dto.response.BookmarkMemoListResponse(mm.id, mm.author, mm.title, mm.createdDate,mm.modifiedDate, true) from BookmarkMemo bmm join bmm.memo mm where bmm.member.id = :memberId and mm.project.id = :projectId",
            countQuery = "select count(bmm) from BookmarkMemo bmm join bmm.memo mm where bmm.member.id = :memberId and mm.project.id = :projectId")
    Page<BookmarkMemoListResponse>
    findDtoList(Pageable pageable, @Param("projectId") Long projectId, @Param("memberId") Long memberId);

    @Query("select bmm from BookmarkMemo bmm join fetch bmm.memo where bmm.member.id = :memberId and bmm.memo.id = :memoId")
    Optional<BookmarkMemo> findByMemberIdAndMemoId(@Param("memberId") Long memberId, @Param("memoId") Long memoId);

}
