package fullcare.backend.project.dto.response;

import fullcare.backend.projectmember.domain.ProjectMemberPositionType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProjectMemberListResponse {

    private Long id;
    private String name;
    private String imageUrl;
    private ProjectMemberPositionType position;
    private boolean isLeader;

    @Builder
    public ProjectMemberListResponse(Long id, String name, String imageUrl, ProjectMemberPositionType position, boolean isLeader) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.position = position;
        this.isLeader = isLeader;
    }
}
