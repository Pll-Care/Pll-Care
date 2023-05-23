package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import lombok.Data;

@Data
public class MidtermListResponse {
    private Long memberId;
    private String name;
//    private MidtermDetailResponse midtermDetailResponses;
private EvaluationBadge evaluationBadge;
    private Long quantity;
//    public MidtermListResponse(Long id, String name, MidtermDetailResponse midtermDetailResponses) {
//        this.id = id;
//        this.name = name;
//        this.midtermDetailResponses = midtermDetailResponses;
//    }

    public MidtermListResponse(Long memberId, String name, EvaluationBadge evaluationBadge, Long quantity) {
        this.memberId = memberId;
        this.name = name;
        this.evaluationBadge = evaluationBadge;
        this.quantity = quantity;
    }
}
