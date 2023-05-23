package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.domain.FinalEvaluation;
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


    public static FinalEvaluationResponse entityToDto(FinalEvaluation finalEvaluation) {
        return FinalEvaluationResponse.builder()
                .content(finalEvaluation.getContent())
                .memberName(finalEvaluation.getEvaluated().getName())
                .score(finalEvaluation.getScore())
                .build();
    }
}
