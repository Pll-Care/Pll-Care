package fullcare.backend.evaluation.dto;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BadgeDto {
    private EvaluationBadge evaluationBadge;
    private String imageUrl;
}
