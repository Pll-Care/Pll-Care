package fullcare.backend.profile.dto.response;

import lombok.Data;

@Data
public class ProfileImageResponse {
    private Long memberId;
    private String imageUrl;

    public ProfileImageResponse(Long memberId, String imageUrl) {
        this.memberId = memberId;
        this.imageUrl = imageUrl;
    }
}
