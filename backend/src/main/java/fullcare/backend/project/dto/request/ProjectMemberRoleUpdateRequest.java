package fullcare.backend.project.dto.request;

import fullcare.backend.projectmember.domain.ProjectMemberRole;
import lombok.Getter;

@Getter
public class ProjectMemberRoleUpdateRequest {
    private Long pmId;
    private ProjectMemberRole projectMemberRole;
}
