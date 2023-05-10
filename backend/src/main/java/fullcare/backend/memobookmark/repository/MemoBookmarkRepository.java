package fullcare.backend.memobookmark.repository;

import fullcare.backend.member.domain.Member;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.memobookmark.domain.MemoBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemoBookmarkRepository extends JpaRepository<MemoBookmark, Long> {
    Optional<MemoBookmark> findByMemberAndMemo(Member member, Memo memo);
}
