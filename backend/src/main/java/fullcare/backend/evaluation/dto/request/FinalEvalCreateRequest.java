package fullcare.backend.evaluation.dto.request;

import fullcare.backend.evaluation.domain.Score;
import lombok.Getter;

@Getter
public class FinalEvalCreateRequest {

    private Long projectId;
    private Long evaluatedId;
    private Score score;
    private String content;

}
