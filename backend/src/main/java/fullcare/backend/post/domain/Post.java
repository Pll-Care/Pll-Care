package fullcare.backend.post.domain;

import fullcare.backend.global.State;
import fullcare.backend.global.entity.BaseEntity;
import fullcare.backend.likes.domain.Likes;
import fullcare.backend.member.domain.Member;
import fullcare.backend.project.domain.Project;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
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
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "recruit_start_dt", nullable = false)
    private LocalDate recruitStartDate;

    @Column(name = "recruit_end_dt", nullable = false)
    private LocalDate recruitEndDate;

    @Lob
    @Column(name = "reference", nullable = false)
    private String reference;

    @Lob
    @Column(name = "contact", nullable = false)
    private String contact;

    @Lob
    @Column(name = "region", nullable = false)
    private String region;

    @Lob
    @Column(name = "tech_stack", nullable = false)
    private String techStack;


    @BatchSize(size = 50)
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recruitment> recruitments = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Likes> likes = new HashSet<>();  // * 좋아요 갯수는 likes set의 size를 이용

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    @Builder(builderMethodName = "createNewPost")
    public Post(Project project, Member author, String title, String description,
                LocalDate recruitStartDate, LocalDate recruitEndDate,
                String reference, String contact, String region, String techStack, State state) {
        this.project = project;
        this.author = author;
        this.title = title;
        this.description = description;
        this.recruitStartDate = recruitStartDate;
        this.recruitEndDate = recruitEndDate;
        this.reference = reference;
        this.contact = contact;
        this.region = region;
        this.techStack = techStack;
        this.state = state;
    }


    public void updateAll(String title, String description, LocalDate recruitStartDate, LocalDate recruitEndDate, String reference, String contact, String region, String techStack, List<Recruitment> recruitments) {
        this.title = title;
        this.description = description;
        this.recruitStartDate = recruitStartDate;
        this.recruitEndDate = recruitEndDate;
        this.reference = reference;
        this.contact = contact;
        this.region = region;
        this.techStack = techStack;

        updateRecruit(recruitments);
    }

    public void updateRecruit(List<Recruitment> recruitments) {
        this.recruitments.clear();
        this.recruitments.addAll(recruitments);
    }

}
