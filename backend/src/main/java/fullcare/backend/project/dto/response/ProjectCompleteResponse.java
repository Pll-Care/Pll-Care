package fullcare.backend.project.dto.response;

import lombok.Getter;

@Getter
public class ProjectCompleteResponse {

    private boolean isCompleted;

    public ProjectCompleteResponse(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
