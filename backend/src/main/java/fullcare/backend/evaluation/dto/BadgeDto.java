package fullcare.backend.evaluation.dto;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BadgeDto {
    private EvaluationBadge evaluationBadge;
    private Long quantity;
    private String imageUrl;

    public BadgeDto(EvaluationBadge evaluationBadge, String imageUrl) {
        this.evaluationBadge = evaluationBadge;
        this.imageUrl = imageUrl;
    }
}
