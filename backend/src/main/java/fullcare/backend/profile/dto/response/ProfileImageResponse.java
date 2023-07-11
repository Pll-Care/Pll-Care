package fullcare.backend.profile.dto.response;

import lombok.Data;

@Data
public class ProfileImageResponse {
    private Long id;
    private String imageUrl;

    public ProfileImageResponse(Long id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }
}
