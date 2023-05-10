package fullcare.backend.memobookmark.domain;

import fullcare.backend.member.domain.Member;
import fullcare.backend.memo.domain.Memo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemoBookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memobookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id")
    private Memo memo;

    @Column(name = "is_bookmarked")
    private boolean isBookmarked;

    @Builder(builderMethodName = "createNewMemoBookmark")
    public MemoBookmark(Member member, Memo memo) {
        this.member = member;
        this.memo = memo;
    }

    public void mark() {
        this.isBookmarked = true;
    }

    public void unmark() {
        this.isBookmarked = false;
    }
}
