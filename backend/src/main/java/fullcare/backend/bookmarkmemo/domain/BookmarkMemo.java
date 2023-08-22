package fullcare.backend.bookmarkmemo.domain;

import fullcare.backend.global.entity.BaseEntity;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.projectmember.domain.ProjectMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BookmarkMemo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmarkmemo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectmember_id")
    private ProjectMember projectMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id")
    private Memo memo;

    @Builder(builderMethodName = "createNewBookmarkMemo")
    public BookmarkMemo(ProjectMember projectMember, Memo memo) {
        this.projectMember = projectMember;
        this.memo = memo;
    }
}
