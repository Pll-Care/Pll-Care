package fullcare.backend.memo.repository;

import fullcare.backend.memo.domain.Memo;
import fullcare.backend.memo.dto.response.MemoDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    @EntityGraph(attributePaths = {"author"})
    Page<Memo> findMemoListByProjectId(Long projectId, Pageable pageable);

    @Query("select new fullcare.backend.memo.dto.response.MemoDetailResponse(m.id, m.author.nickname, m.title, m.content, case when bmm.id is null then false else true end, m.createdDate, m.modifiedDate) " +
            "from Memo m left outer join BookmarkMemo bmm on m.id = bmm.memo.id and bmm.member.id = :memberId where m.id = :memoId")
    MemoDetailResponse findMemoDetailDto(@Param("memberId") Long memberId, @Param("memoId") Long memoId);

}
