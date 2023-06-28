package fullcare.backend.profile.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class ProjectExperienceResponseDto extends ProjectExperienceRequestDto{
    private Long projectId;
    @Builder(builderMethodName = "createResponseDto")
    public ProjectExperienceResponseDto(Long projectId, String title, String description, LocalDate startDate, LocalDate endDate, String techStack) {
        super(title, description, startDate, endDate, techStack);
        this.projectId = projectId;
    }
}
