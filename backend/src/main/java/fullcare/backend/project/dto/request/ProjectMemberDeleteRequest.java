package fullcare.backend.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ProjectMemberDeleteRequest {

    @NotBlank
    private Long projectId;

    @NotBlank
    private Long memberId;
}
