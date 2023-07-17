package fullcare.backend.profile.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fullcare.backend.util.dto.TechStackDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProjectExperienceDto{
    private Long projectId;
    private String title;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;
    private List<TechStackDto> techStack;
    @Builder(builderMethodName = "createResponseDto")
    public ProjectExperienceDto(Long projectId, String title, String description, LocalDate startDate, LocalDate endDate, List<TechStackDto> techStack) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.techStack = techStack;
    }



}
