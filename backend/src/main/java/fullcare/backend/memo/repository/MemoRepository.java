package fullcare.backend.memo.repository;

import fullcare.backend.memo.domain.Memo;
import fullcare.backend.memo.dto.response.MemoDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    @Query(value = "select m from Memo m join fetch m.author a join fetch a.member where a.project.id =:projectId",
            countQuery = "select m from Memo m join m.author a where a.project.id =:projectId")
    Page<Memo> findListByProjectId(@Param("projectId") Long projectId, Pageable pageable);

    @Query("select new fullcare.backend.memo.dto.response.MemoDetailResponse(m.id, m.author.member.name, m.author.id ,m.title, m.content, case when bmm.id is null then false else true end, m.createdDate, m.modifiedDate) " +
            "from Memo m left outer join BookmarkMemo bmm on m.id = bmm.memo.id and bmm.projectMember.id = :projectMemberId where m.id = :memoId")
    Optional<MemoDetailResponse> findMemoDetailResponse(@Param("projectMemberId") Long projectMemberId, @Param("memoId") Long memoId);

}
