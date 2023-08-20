package fullcare.backend.recruitment.domain;

import fullcare.backend.global.errorcode.PostErrorCode;
import fullcare.backend.global.exceptionhandling.exception.InvalidRecruimentException;
import fullcare.backend.global.exceptionhandling.exception.UnauthorizedAccessException;
import fullcare.backend.post.domain.Post;
import fullcare.backend.projectmember.domain.ProjectMemberPositionType;
import jakarta.persistence.*;
import lombok.*;

@ToString(of = {"id", "recruitPosition", "amount"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(name = "postion")
    private ProjectMemberPositionType recruitPosition;

    @Column
    private long currentAmount;

    @Column
    private long totalAmount;

    @Builder(builderMethodName = "createNewRecruitment")
    public Recruitment(Post post, ProjectMemberPositionType recruitPosition, long currentAmount, long totalAmount) {
        this.post = post;

        if (recruitPosition == ProjectMemberPositionType.미정) {
            throw new InvalidRecruimentException(PostErrorCode.INVALID_RECRUITMENT_POSITION);
        }

        this.recruitPosition = recruitPosition;

        if (currentAmount > totalAmount) {
            throw new InvalidRecruimentException(PostErrorCode.INVALID_RECRUITMENT_AMOUNT);
        }

        this.currentAmount = currentAmount;
        this.totalAmount = totalAmount;
    }

    public void updateRecruitment() {
        if (this.currentAmount < this.totalAmount) {
            this.currentAmount += 1;
        } else {
            throw new UnauthorizedAccessException(PostErrorCode.RECRUITMENT_COMPLETED);
        }
    }
}
