package fullcare.backend.project.dto.response;

import fullcare.backend.projectmember.domain.ProjectMemberPositionType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProjectMemberListResponse {

    private Long memberId;
    private String name;
    private String imageUrl;
    private ProjectMemberPositionType position;
    private boolean isLeader;

    @Builder
    public ProjectMemberListResponse(Long memberId, String name, String imageUrl, ProjectMemberPositionType position, boolean isLeader) {
        this.memberId = memberId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.position = position;
        this.isLeader = isLeader;
    }
}
