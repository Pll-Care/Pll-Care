package fullcare.backend.project.dto.request;

import fullcare.backend.projectmember.domain.ProjectMemberPositionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProjectMemberPositionUpdateRequest {

    @NotNull
    private Long memberId;
    private ProjectMemberPositionType position;

}
