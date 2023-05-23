package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MidtermDetailResponse {
    private EvaluationBadge evaluationBadge;
    private Long quantity;

    public MidtermDetailResponse(EvaluationBadge evaluationBadge, Long quantity) {
        this.evaluationBadge = evaluationBadge;
        this.quantity = quantity;
    }
}
