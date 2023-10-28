package fullcare.backend.profile.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProjectExperienceResponseDto {
    private int year;
    private List<ProjectExperienceDto> projectExperiences;

    public ProjectExperienceResponseDto(int year, List<ProjectExperienceDto> projectExperiences) {
        this.year = year;
        this.projectExperiences = projectExperiences;
    }
}
