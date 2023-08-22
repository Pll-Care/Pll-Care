package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.dto.ScoreDto;
import lombok.Builder;
import lombok.Data;

@Data
public class MyEvalListResponse {
    private Long projectId;
    private String title;
    private ScoreDto score;
    @Builder
    public MyEvalListResponse(Long projectId, String title, ScoreDto score) {
        this.projectId = projectId;
        this.title = title;
        this.score = score;
    }

}
