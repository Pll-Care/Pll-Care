package fullcare.backend.profile.dto.response;

import fullcare.backend.post.domain.RecruitPosition;
import lombok.Data;

import java.util.List;

@Data
public class ProfileTechStackResponse {
    private RecruitPosition recruitPosition;
    private List<String> techStack;
    private boolean myProfile;

    public ProfileTechStackResponse(RecruitPosition recruitPosition, List<String> techStack, boolean myProfile) {
        this.recruitPosition = recruitPosition;
        this.techStack = techStack;
        this.myProfile = myProfile;
    }
}
