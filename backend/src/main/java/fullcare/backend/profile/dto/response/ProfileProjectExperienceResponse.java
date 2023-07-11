package fullcare.backend.profile.dto.response;

import fullcare.backend.profile.dto.ProjectExperienceDto;
import fullcare.backend.profile.dto.ProjectExperienceResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ProfileProjectExperienceResponse {
    private List<ProjectExperienceResponseDto> data;
    private boolean myProfile;

    @Builder
    public ProfileProjectExperienceResponse(List<ProjectExperienceResponseDto> data, boolean myProfile) {
        this.data = data;
        this.myProfile = myProfile;
    }
}
