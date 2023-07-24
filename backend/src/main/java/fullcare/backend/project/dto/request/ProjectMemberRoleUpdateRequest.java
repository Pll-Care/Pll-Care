package fullcare.backend.project.dto.request;

import fullcare.backend.projectmember.domain.ProjectMemberRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProjectMemberRoleUpdateRequest {

    @NotNull
    private Long memberId;
    private ProjectMemberRole projectMemberRole;
}
