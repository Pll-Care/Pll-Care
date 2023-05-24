package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.dto.BadgeDto;
import fullcare.backend.evaluation.dto.MidTermRankingDto;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MidTermEvalListResponse {
    private Long memberId;
    private String name;
    private String imageUrl;
    private List<BadgeDto> badges = new ArrayList<>();
    private List<MidTermRankingDto> ranking = new ArrayList<>();
    @Builder
    public MidTermEvalListResponse(Long memberId, String name, String imageUrl) {
        this.memberId = memberId;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public void addBadge(BadgeDto badgeDto){
        this.badges.add(badgeDto);
    }
}
