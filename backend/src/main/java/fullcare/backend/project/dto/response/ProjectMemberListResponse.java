package fullcare.backend.project.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class ProjectMemberListResponse {
    private Long id;
    private String name;
    @Builder
    public ProjectMemberListResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
