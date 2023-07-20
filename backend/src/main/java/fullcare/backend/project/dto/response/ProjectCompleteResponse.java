package fullcare.backend.project.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProjectCompleteResponse {

    private boolean isCompleted;

    @Builder
    public ProjectCompleteResponse(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
