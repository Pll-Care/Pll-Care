package fullcare.backend.evaluation.dao;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import lombok.Data;

@Data
public class BadgeDao {
    private Long memberId;
    private String name;
    private EvaluationBadge evaluationBadge;
    private Long quantity;


    public BadgeDao(Long memberId, String name, EvaluationBadge evaluationBadge, Long quantity) {
        this.memberId = memberId;
        this.name = name;
        this.evaluationBadge = evaluationBadge;
        this.quantity = quantity;
    }
}
