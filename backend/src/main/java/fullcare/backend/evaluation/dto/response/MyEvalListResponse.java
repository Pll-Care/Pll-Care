package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.dto.BadgeDto;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MyEvalListResponse {
    private Long projectId;
    private String projectTitle;
    private List<BadgeDto> badges = new ArrayList<>();
    @Builder
    public MyEvalListResponse(Long projectId, String projectTitle) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
    }
    public void addBadge(BadgeDto badgeDto){
        this.badges.add(badgeDto);
    }
}
