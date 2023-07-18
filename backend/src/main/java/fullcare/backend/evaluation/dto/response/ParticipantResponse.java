package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.dto.BadgeDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ParticipantResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private boolean isMe;
    private List<BadgeDto> badgeDtos = new ArrayList<>();
    private Long finalEvalId;
    @Builder
    public ParticipantResponse(Long id, String name, String imageUrl, boolean isMe) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.isMe = isMe;
    }
    public void addBadge(BadgeDto badgeDto){
        this.badgeDtos.add(badgeDto);
    }
}
