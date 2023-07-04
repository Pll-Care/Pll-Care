package fullcare.backend.profile.dto.response;

import fullcare.backend.profile.dto.ProjectExperienceResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ProfileProjectExperienceResponse {
    private List<ProjectExperienceResponseDto> projectExperiences;
    private boolean myProfile;

    @Builder
    public ProfileProjectExperienceResponse(List<ProjectExperienceResponseDto> projectExperiences, boolean myProfile) {
        this.projectExperiences = projectExperiences;
        this.myProfile = myProfile;
    }
}
