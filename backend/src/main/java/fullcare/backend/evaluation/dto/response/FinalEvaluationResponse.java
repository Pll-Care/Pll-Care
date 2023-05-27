package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.domain.FinalTermEvaluation;
import fullcare.backend.evaluation.domain.Score;
import fullcare.backend.global.State;
import lombok.Builder;
import lombok.Data;

@Data
public class FinalEvaluationResponse {


    private String memberName;
    private String content;
    private Score score;
    private State state;

    @Builder
    public FinalEvaluationResponse(String memberName, String content, Score score, State state) {
        this.memberName = memberName;
        this.content = content;
        this.score = score;
        this.state = state;
    }


    public static FinalEvaluationResponse entityToDto(FinalTermEvaluation finalTermEvaluation) {
        return FinalEvaluationResponse.builder()
                .content(finalTermEvaluation.getContent())
                .memberName(finalTermEvaluation.getEvaluated().getName())
                .score(finalTermEvaluation.getScore())
                .state(finalTermEvaluation.getState())
                .build();
    }
}
