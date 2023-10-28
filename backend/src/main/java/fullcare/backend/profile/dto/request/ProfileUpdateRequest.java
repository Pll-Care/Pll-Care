package fullcare.backend.profile.dto.request;

import fullcare.backend.profile.domain.Contact;
import fullcare.backend.profile.dto.ProjectExperienceRequestDto;
import fullcare.backend.recruitment.domain.RecruitPosition;
import fullcare.backend.util.dto.TechStack;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProfileUpdateRequest {
    private Contact contact;
    private RecruitPosition recruitPosition;
    private List<TechStack> techStack = new ArrayList<>();
    private Long projectId;
    private List<ProjectExperienceRequestDto> projectExperiences;
    private boolean delete;

}
