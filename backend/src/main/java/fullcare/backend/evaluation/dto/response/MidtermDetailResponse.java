package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import fullcare.backend.evaluation.dto.BadgeDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MidtermDetailResponse extends BadgeDto {
    public MidtermDetailResponse(EvaluationBadge evaluationBadge, Long quantity) {
        super(evaluationBadge, quantity);
    }

}
