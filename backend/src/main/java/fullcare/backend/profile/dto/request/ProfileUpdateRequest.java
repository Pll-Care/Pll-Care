package fullcare.backend.profile.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import fullcare.backend.post.domain.RecruitPosition;
import fullcare.backend.profile.domain.Contact;
import fullcare.backend.profile.dto.ProjectExperienceDto;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ProfileUpdateRequest {
    private Contact contact;
    private RecruitPosition recruitPosition;
    private String techStack;
    private Long projectId;
    private List<ProjectExperienceDto> projectExperiences;
    private boolean delete;
}
