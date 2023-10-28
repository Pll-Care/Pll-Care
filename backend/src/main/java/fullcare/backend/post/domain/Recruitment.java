package fullcare.backend.post.domain;

import jakarta.persistence.*;
import lombok.*;

@ToString(of = {"id", "recruitPosition", "amount"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruiement_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(name = "postion")
    private RecruitPosition recruitPosition;

    @Column
    private long amount;

    @Builder(builderMethodName = "createNewRecruitment")
    public Recruitment(Post post, RecruitPosition recruitPosition, long amount) {
        this.post = post;
        this.recruitPosition = recruitPosition;
        this.amount = amount;
    }
}
