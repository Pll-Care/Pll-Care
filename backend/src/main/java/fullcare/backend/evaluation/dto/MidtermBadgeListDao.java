package fullcare.backend.evaluation.dto;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import lombok.Data;

@Data
public class MidtermBadgeListDao {
    private Long memberId;
    private String name;
    private EvaluationBadge evaluationBadge;
    private Long quantity;


    public MidtermBadgeListDao(Long memberId, String name, EvaluationBadge evaluationBadge, Long quantity) {
        this.memberId = memberId;
        this.name = name;
        this.evaluationBadge = evaluationBadge;
        this.quantity = quantity;
    }
}
