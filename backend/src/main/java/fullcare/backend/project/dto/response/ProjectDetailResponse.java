package fullcare.backend.project.dto.response;

import fullcare.backend.global.State;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProjectDetailResponse {

    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String imageUrl;
    private State state;

    @Builder
    public ProjectDetailResponse(String title, String description, LocalDate startDate, LocalDate endDate, String imageUrl, State state) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
        this.state = state;
    }

}
