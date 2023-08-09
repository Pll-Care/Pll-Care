package fullcare.backend.post.domain;

import fullcare.backend.apply.domain.Apply;
import fullcare.backend.global.State;
import fullcare.backend.global.entity.BaseEntity;
import fullcare.backend.global.errorcode.PostErrorCode;
import fullcare.backend.global.exceptionhandling.exception.InvalidDateRangeException;
import fullcare.backend.likes.domain.Likes;
import fullcare.backend.member.domain.Member;
import fullcare.backend.project.domain.Project;
import fullcare.backend.recruitment.domain.Recruitment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Formula;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString(of = {"id", "title"})
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

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recruitment> recruitments = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Likes> likes = new HashSet<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Apply> applies = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    // ! 좋아요 갯수
    @Formula("(SELECT count(1) FROM LIKES l WHERE l.post_id = post_id)")
    private int likeCount;

    @Builder(builderMethodName = "createNewPost")
    public Post(Project project, Member author, String title, String description,
                LocalDate recruitStartDate, LocalDate recruitEndDate,
                String reference, String contact, String region, String techStack, State state) {
        this.project = project;
        this.author = author;
        this.title = title;
        this.description = description;

        if (recruitStartDate.isAfter(recruitEndDate)) {
            throw new InvalidDateRangeException(PostErrorCode.INVALID_DATE_RANGE); // todo 모집 날짜 자체에 대한 검증 (프로젝트 일정에 포함되는 기간인지 검증 X)
        }

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

        updateRecruitments(recruitments);
    }

    public void updateRecruitments(List<Recruitment> recruitments) {
        this.recruitments.clear();
        this.recruitments.addAll(recruitments);
    }

    public void completed() {
        this.state = State.COMPLETE;
    }
}
