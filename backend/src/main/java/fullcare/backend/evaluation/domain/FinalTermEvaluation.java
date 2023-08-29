package fullcare.backend.evaluation.domain;


import fullcare.backend.evaluation.dto.request.FinalEvalUpdateRequest;
import fullcare.backend.global.State;
import fullcare.backend.member.domain.Member;
import fullcare.backend.project.domain.Project;
import fullcare.backend.projectmember.domain.ProjectMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FinalTermEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "final_eval_id")
    private Long id;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Embedded
    private Score score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluator_id", nullable = false)
    private ProjectMember evaluator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluated_id", nullable = false)
    private ProjectMember evaluated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;


    @Builder(builderMethodName = "createNewFinalEval")
    public FinalTermEvaluation(String content, Score score, ProjectMember evaluator, ProjectMember evaluated, Project project) {
        this.content = content;
        this.score = score;
        this.evaluator = evaluator;
        this.evaluated = evaluated;
        this.project = project;
    }
    public void update(FinalEvalUpdateRequest finalEvalUpdateRequest){
        this.content = finalEvalUpdateRequest.getContent();
        this.score = finalEvalUpdateRequest.getScore();
    }
}
