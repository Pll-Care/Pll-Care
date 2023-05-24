package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import fullcare.backend.evaluation.dto.Badge;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MidtermDetailResponse extends Badge {
    public MidtermDetailResponse(EvaluationBadge evaluationBadge, Long quantity) {
        super(evaluationBadge, quantity);
    }

}
