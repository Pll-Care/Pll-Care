package fullcare.backend.evaluation.dto.request;

import fullcare.backend.evaluation.domain.Score;
import fullcare.backend.global.State;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FinalEvalCreateRequest {

    @NotNull
    private Long projectId;

    @NotNull
    private Long evaluatedId;

    // TODO 값 타입을 DTO에서 써도 되는가?
    @NotNull
    private Score score;

    @NotNull
    private String content;

//    // ?
//    @NotNull
//    private State state;

}
