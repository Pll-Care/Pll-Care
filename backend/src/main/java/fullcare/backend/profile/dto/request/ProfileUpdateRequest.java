package fullcare.backend.profile.dto.request;

import fullcare.backend.post.domain.RecruitPosition;
import fullcare.backend.profile.domain.Contact;
import fullcare.backend.profile.dto.ProjectExperienceRequestDto;
import lombok.Getter;

import java.util.List;

@Getter
public class ProfileUpdateRequest {
    private Contact contact;
    private RecruitPosition recruitPosition;
    private String techStack;
    private Long projectId;
    private List<ProjectExperienceRequestDto> projectExperiences;
    private boolean delete;
}
