package fullcare.backend.evaluation.dto.response;

import lombok.Data;

@Data
public class FinalEvaluationCreateResponse {
    private Long finalEvalId;

    public FinalEvaluationCreateResponse(Long finalEvalId) {
        this.finalEvalId = finalEvalId;
    }
}
