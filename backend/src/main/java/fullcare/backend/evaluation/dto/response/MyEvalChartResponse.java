package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.domain.Score;
import fullcare.backend.evaluation.dto.ScoreDto;
import lombok.Data;

@Data
public class MyEvalChartResponse {
    private ScoreDto score;

    public MyEvalChartResponse(ScoreDto score) {
        this.score = score;
    }
}
