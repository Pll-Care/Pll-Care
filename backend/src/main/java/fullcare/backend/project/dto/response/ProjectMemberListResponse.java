package fullcare.backend.project.dto.response;

import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import lombok.Builder;
import lombok.Data;

@Data
public class ProjectMemberListResponse {

    private Long id;
    private String name;
    private String imageUrl;
    private ProjectMemberRoleType position;
    private boolean isLeader;

    @Builder
    public ProjectMemberListResponse(Long id, String name, String imageUrl, ProjectMemberRoleType position, boolean isLeader) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.position = position;
        this.isLeader = isLeader;
    }
    
}
