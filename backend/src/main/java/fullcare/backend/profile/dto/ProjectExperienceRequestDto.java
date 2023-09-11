package fullcare.backend.profile.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import fullcare.backend.util.dto.TechStack;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectExperienceRequestDto {
    private String title;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;
    private List<TechStack> techStack;
    @Builder
    public ProjectExperienceRequestDto(String title, String description, LocalDate startDate, LocalDate endDate, List<TechStack> techStack) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.techStack = techStack;
    }
}
