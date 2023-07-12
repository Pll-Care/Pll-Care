package fullcare.backend.project.dto.response;

import lombok.Getter;

@Getter
public class ProjectSimpleListResponse {
    private Long projectId;
    private String title;

    public ProjectSimpleListResponse(Long projectId, String title) {
        this.projectId = projectId;
        this.title = title;
    }
}
