package fullcare.backend.profile.dto.response;

import fullcare.backend.post.domain.RecruitPosition;
import fullcare.backend.util.dto.TechStack;
import fullcare.backend.util.dto.TechStackDto;
import lombok.Data;

import java.util.List;

@Data
public class ProfileTechStackResponse {
    private RecruitPosition recruitPosition;
    private List<TechStackDto> techStack;
    private boolean myProfile;

    public ProfileTechStackResponse(RecruitPosition recruitPosition, List<TechStackDto> techStack, boolean myProfile) {
        this.recruitPosition = recruitPosition;
        this.techStack = techStack;
        this.myProfile = myProfile;
    }
}
