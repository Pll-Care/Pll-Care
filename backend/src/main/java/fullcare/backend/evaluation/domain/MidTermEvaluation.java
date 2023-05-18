package fullcare.backend.evaluation.domain;

import fullcare.backend.member.domain.Member;
import fullcare.backend.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MidTermEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mid_eval_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="schedule_id", nullable = false)
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    private EvaluationBadge evaluationBadge;
}
