package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.dto.BadgeDto;
import fullcare.backend.evaluation.dto.FinalEvalDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class MyEvalDetailResponse {
    private List<BadgeDto> badges = new ArrayList<>();
    private List<FinalEvalDto> finalEvals = new ArrayList();

    public MyEvalDetailResponse(List<BadgeDto> badges, List<FinalEvalDto> finalEvals) {
        this.badges = badges;
        this.finalEvals = finalEvals;
    }
}
