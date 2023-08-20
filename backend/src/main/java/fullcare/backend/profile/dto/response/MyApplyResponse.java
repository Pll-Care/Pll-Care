package fullcare.backend.profile.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class MyApplyResponse {
    private Long postId;
    private String title;
    private String description;

    @Builder
    public MyApplyResponse(Long postId, String title, String description) {
        this.postId = postId;
        this.title = title;
        this.description = description;
    }
}
