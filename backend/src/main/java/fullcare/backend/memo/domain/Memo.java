package fullcare.backend.memo.domain;

import fullcare.backend.bookmarkmemo.domain.BookmarkMemo;
import fullcare.backend.global.entity.BaseEntity;
import fullcare.backend.project.domain.Project;
import fullcare.backend.projectmember.domain.ProjectMember;
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
    private ProjectMember author;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "memo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookmarkMemo> bookmarkMemos = new ArrayList<>();

    @Builder(builderMethodName = "createNewMemo")
    public Memo(Project project, ProjectMember author, String title, String content) {
        this.project = project;
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public void updateAll(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
