package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.dto.BadgeDto;
import fullcare.backend.evaluation.dto.FinalEvalDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class MyEvalDetailResponse {
    private List<BadgeDto> badgeDtos = new ArrayList<>();
    private List<FinalEvalDto> finalEvals = new ArrayList();

    public MyEvalDetailResponse(List<BadgeDto> badgeDtos, List<FinalEvalDto> finalEvals) {
        this.badgeDtos = badgeDtos;
        this.finalEvals = finalEvals;
    }
}
