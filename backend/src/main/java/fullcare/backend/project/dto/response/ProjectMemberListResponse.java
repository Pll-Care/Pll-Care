package fullcare.backend.project.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class ProjectMemberListResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private String position;
    @Builder
    public ProjectMemberListResponse(Long id, String name, String imageUrl, String position) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.position = position;
    }
}
