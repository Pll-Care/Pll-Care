package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.dto.BadgeDto;
import fullcare.backend.evaluation.dto.BadgeSubDto;
import fullcare.backend.evaluation.dto.FinalEvalDto;
import fullcare.backend.evaluation.dto.ScoreDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class MyEvalDetailResponse {
    private BadgeSubDto badges;
    private List<FinalEvalDto> finalEvals = new ArrayList();
//    private ScoreDto score;

    public MyEvalDetailResponse(BadgeSubDto badges, List<FinalEvalDto> finalEvals) {
        this.badges = badges;
        this.finalEvals = finalEvals;
//        this.score = scoreDto;
    }
}
