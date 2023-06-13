package fullcare.backend.evaluation.domain;

import fullcare.backend.member.domain.Member;
import fullcare.backend.project.domain.Project;
import fullcare.backend.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MidtermEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mid_eval_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id", nullable = false)
    private Member voter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voted_id", nullable = false)
    private Member voted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    private EvaluationBadge evaluationBadge;


    @Builder(builderMethodName = "createNewMidtermEval")
    public MidtermEvaluation(Member voter, Member voted, Project project, Schedule schedule, EvaluationBadge evaluationBadge) {
        this.voter = voter;
        this.voted = voted;
        this.project = project;
        this.schedule = schedule;
        this.evaluationBadge = evaluationBadge;
    }
}
