package fullcare.backend.profile.dto.response;

import lombok.Data;

@Data
public class ValidateMyProfileResponse {
    private boolean myProfile;

    public ValidateMyProfileResponse(boolean myProfile) {
        this.myProfile = myProfile;
    }
}
