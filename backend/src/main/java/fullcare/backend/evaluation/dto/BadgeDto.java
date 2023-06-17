package fullcare.backend.evaluation.dto;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class BadgeDto {
    private EvaluationBadge evaluationBadge;
    private Long quantity;

    public BadgeDto(EvaluationBadge evaluationBadge, Long quantity) {
        this.evaluationBadge = evaluationBadge;
        this.quantity = quantity;
    }


}
