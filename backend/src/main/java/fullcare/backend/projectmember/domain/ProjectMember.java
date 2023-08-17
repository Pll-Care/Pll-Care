package fullcare.backend.projectmember.domain;

import fullcare.backend.bookmarkmemo.domain.BookmarkMemo;
import fullcare.backend.member.domain.Member;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.post.domain.Post;
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
@Entity(name = "project_member")
@Table(name = "project_member")
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    //    @OneToMany(mappedBy = "projectMember", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Memo> memos = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Embedded
    private ProjectMemberType projectMemberType; // 포지션 + 역할


    @Builder(builderMethodName = "createNewProjectMember")
    public ProjectMember(Member member, Project project, ProjectMemberType projectMemberType) {
        this.member = member;
        this.project = project;
        this.projectMemberType = projectMemberType;

    }

    public boolean isLeader() {
        return (this.projectMemberType.getRole() == ProjectMemberRoleType.리더) ? true : false;
    }

    public void updateRole(ProjectMemberRoleType projectMemberRoleType) {
        this.projectMemberType.updateRole(projectMemberRoleType);
    }

    public void updatePosition(ProjectMemberPositionType projectMemberPositionType) {
        this.projectMemberType.updatePosition(projectMemberPositionType);
    }

    public BookmarkMemo bookmark(Memo memo) {
        BookmarkMemo bookmarkMemo = BookmarkMemo.createNewBookmarkMemo()
                .projectMember(this)
                .memo(memo)
                .build();

        return bookmarkMemo;
    }
}
