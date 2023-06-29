package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.domain.Score;
import lombok.Data;

@Data
public class MyEvalChartResponse {
    private Score score;

    public MyEvalChartResponse(Score score) {
        this.score = score;
    }
}
