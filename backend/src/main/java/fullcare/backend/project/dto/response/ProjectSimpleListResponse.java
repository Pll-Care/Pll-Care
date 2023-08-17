package fullcare.backend.project.dto.response;

import lombok.Getter;

@Getter
public class ProjectSimpleListResponse {
    private Long projectId;
    private String imageUrl;
    private String title;
    
    public ProjectSimpleListResponse(Long projectId, String imageUrl, String title) {
        this.projectId = projectId;
        this.imageUrl = imageUrl;
        this.title = title;
    }

}
