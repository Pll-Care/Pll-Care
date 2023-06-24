package fullcare.backend.project.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class ProjectMemberListResponse {
    private Long id;
    private Long pmId;
    private String name;
    private String imageUrl;
    private String position;

    @Builder
    public ProjectMemberListResponse(Long id, Long pmId, String name, String imageUrl, String position) {
        this.id = id;
        this.pmId = pmId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.position = position;
    }
}
