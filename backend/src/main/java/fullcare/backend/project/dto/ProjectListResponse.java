package fullcare.backend.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ProjectListResponse {
    private Long projectId;
    private String title;
    private String content;
    @Builder
    public ProjectListResponse(Long projectId, String title, String content) {
        this.projectId = projectId;
        this.title = title;
        this.content = content;
    }
}
