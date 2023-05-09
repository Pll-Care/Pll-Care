package fullcare.backend.post.domain;

import fullcare.backend.likes.domain.Likes;
import fullcare.backend.member.domain.Member;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.domain.State;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id",nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="author_id", nullable = false)
    private Member member;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "create_dt", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_dt", nullable = false)
    private LocalDateTime modifiedDate;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Likes> likes = new HashSet<>();

    @Column(name = "likes_count", nullable = false)
    private long likesCount;

    // todo -> private long viewCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    @Lob
    @Column(name ="tech_stack", nullable = false)
    private String techStack;





}
