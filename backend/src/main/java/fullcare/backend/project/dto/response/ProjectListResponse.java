package fullcare.backend.project.dto.response;

import fullcare.backend.global.State;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectListResponse {
    private Long projectId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private State state;

    @Builder
    public ProjectListResponse(Long projectId, String title, String description, LocalDate startDate, LocalDate endDate, State state) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
    }

}
