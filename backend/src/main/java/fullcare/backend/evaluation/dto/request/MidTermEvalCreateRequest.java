package fullcare.backend.evaluation.dto.request;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MidTermEvalCreateRequest {
    @NotNull
    private Long projectId;
    @NotNull
    private Long votedId;
    @NotNull
    private Long scheduleId;
    @NotNull
    private EvaluationBadge evaluationBadge;

}
