package fullcare.backend.evaluation.dto;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import lombok.*;

@Getter
@Setter
@ToString
public class BadgeDto extends Badge {

    private String imageUrl;

    public BadgeDto(EvaluationBadge evaluationBadge, String imageUrl) {
        super(evaluationBadge, 0l);
        this.imageUrl = imageUrl;
    }

    public BadgeDto(EvaluationBadge evaluationBadge, Long quantity, String imageUrl) {
        super(evaluationBadge, quantity);
        this.imageUrl = imageUrl;
    }
}
