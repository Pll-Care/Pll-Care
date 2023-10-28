package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.domain.FinalTermEvaluation;
import fullcare.backend.evaluation.domain.Score;
import lombok.Builder;
import lombok.Data;

@Data
public class FinalEvaluationResponse {


    private String name;
    private String content;
    private Score score;

    @Builder
    public FinalEvaluationResponse(String name, String content, Score score) {
        this.name = name;
        this.content = content;
        this.score = score;
    }


    public static FinalEvaluationResponse entityToDto(FinalTermEvaluation finalTermEvaluation) {
        return FinalEvaluationResponse.builder()
                .content(finalTermEvaluation.getContent())
                .name(finalTermEvaluation.getEvaluated().getMember().getName())
                .score(finalTermEvaluation.getScore())
                .build();
    }
}
