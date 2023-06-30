package fullcare.backend.memo.domain;

import fullcare.backend.global.entity.BaseEntity;
import fullcare.backend.member.domain.Member;
import fullcare.backend.project.domain.Project;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Memo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "memo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookmarkMemo> bookmarkMemos = new ArrayList<>();

    @Builder(builderMethodName = "createNewMemo")
    public Memo(Project project, String title, String content, Member author) {
        this.project = project;
        this.title = title;
        this.content = content;
        this.author = author;

        project.getMemos().add(this); // todo ! 반영되는지 확인 필요 (무조건 확인해야함)
    }

//    public static Memo createNewMemo(Project project, String title, String content) {
//        Memo newMemo = Memo.
//                .title(title)
//                .content(content)
//                .build();
//
//        newMemo.belongTo(project);
//        return newMemo;
//    }

    public void updateAll(String title, String content) {
        this.title = title;
        this.content = content;
    }

//    private void belongTo(Project project) {
//        this.project = project;
//        project.getMemos().add(this);
//    }

}
