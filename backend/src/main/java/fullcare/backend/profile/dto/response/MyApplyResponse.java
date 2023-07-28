package fullcare.backend.profile.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class MyApplyResponse {
    private Long id;
    private String title;
    private String description;
    @Builder
    public MyApplyResponse(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
