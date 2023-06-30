package fullcare.backend.project.dto.request;

import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProjectMemberAddRequest {

    @NotNull
    private Long projectId;

    @NotNull
    private Long memberId;

    private ProjectMemberRoleType position;
}
