package fullcare.backend.profile.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProfileBioUpdateRequest {
    @NotNull
    private String bio;
    @NotNull
    private String nickname;
    @NotNull
    private String imageUrl;
}
