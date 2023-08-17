package fullcare.backend.bookmarkmemo.repository;

import fullcare.backend.bookmarkmemo.domain.BookmarkMemo;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.projectmember.domain.ProjectMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookmarkMemoRepository extends JpaRepository<BookmarkMemo, Long> {
    Optional<BookmarkMemo> findByMemoAndProjectMember(Memo memo, ProjectMember projectMember);

    @Query(value = "select bmm from BookmarkMemo bmm join fetch bmm.memo m join m.author a join fetch a.member where bmm.projectMember.id =:projectMemberId",
            countQuery = "select bmm from BookmarkMemo bmm where bmm.projectMember.id =:projectMemberId")
    Page<BookmarkMemo> findList(@Param("projectMemberId") Long projectMemberId, Pageable pageable);

}
