package fullcare.backend.project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProjectLeaderChangeRequest {

    @NotNull
    private Long memberId;
}
