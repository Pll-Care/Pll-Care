package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.domain.Score;
import fullcare.backend.evaluation.dto.BadgeDto;
import fullcare.backend.evaluation.dto.ScoreDto;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MyEvalListResponse {
    private Long projectId;
    private String projectTitle;
    private ScoreDto score;
//    private List<BadgeDto> badgeDtos = new ArrayList<>();
    @Builder
    public MyEvalListResponse(Long projectId, String projectTitle, ScoreDto score) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.score = score;
    }
//    public void addBadge(BadgeDto badgeDto){
//        this.badgeDtos.add(badgeDto);
//    }
}
