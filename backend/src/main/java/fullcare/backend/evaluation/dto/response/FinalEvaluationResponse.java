package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.domain.FinalTermEvaluation;
import fullcare.backend.evaluation.domain.Score;
import lombok.Builder;

public class FinalEvaluationResponse {


    private String memberName;
    private String content;
    private Score score;


    @Builder
    public FinalEvaluationResponse(String memberName, String content, Score score) {
        this.memberName = memberName;
        this.content = content;
        this.score = score;
    }


    public static FinalEvaluationResponse entityToDto(FinalTermEvaluation finalTermEvaluation) {
        return FinalEvaluationResponse.builder()
                .content(finalTermEvaluation.getContent())
                .memberName(finalTermEvaluation.getEvaluated().getName())
                .score(finalTermEvaluation.getScore())
                .build();
    }
}
