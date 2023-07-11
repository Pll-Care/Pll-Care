package fullcare.backend.profile.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class ProjectExperienceDto extends ProjectExperienceRequestDto{
    private Long projectId;
    @Builder(builderMethodName = "createResponseDto")
    public ProjectExperienceDto(Long projectId, String title, String description,
        LocalDate startDate,
        LocalDate endDate, String techStack) {
        super(title, description, startDate, endDate, techStack);
        this.projectId = projectId;
    }
}
