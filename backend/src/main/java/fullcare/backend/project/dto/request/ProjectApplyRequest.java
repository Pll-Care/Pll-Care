package fullcare.backend.project.dto.request;

import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import lombok.Getter;

@Getter
public class ProjectApplyRequest {

    private ProjectMemberRoleType position;
}
