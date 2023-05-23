package fullcare.backend.evaluation.dto.request;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import lombok.Getter;

@Getter
public class MidTermEvalCreateRequest {

    private Long projectId;
    private Long votedId;
    private EvaluationBadge evaluationBadge;

}
