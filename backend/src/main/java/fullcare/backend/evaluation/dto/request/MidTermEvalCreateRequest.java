package fullcare.backend.evaluation.dto.request;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MidTermEvalCreateRequest {

    @NotBlank
    private Long projectId;

    @NotBlank
    private Long votedId;

    @NotBlank
    private Long scheduleId;

    // ?
    @NotNull
    private EvaluationBadge evaluationBadge;

    public MidTermEvalCreateRequest(Long projectId, Long votedId, Long scheduleId, EvaluationBadge evaluationBadge) {
        this.projectId = projectId;
        this.votedId = votedId;
        this.scheduleId = scheduleId;
        this.evaluationBadge = evaluationBadge;
    }
}
