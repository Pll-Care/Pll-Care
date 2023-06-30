package fullcare.backend.project.dto.response;

import lombok.Data;

@Data
public class ProjectUpdateStateResponse {

    private Long projectId;
    public ProjectUpdateStateResponse(Long projectId) {
        this.projectId = projectId;
    }

}
