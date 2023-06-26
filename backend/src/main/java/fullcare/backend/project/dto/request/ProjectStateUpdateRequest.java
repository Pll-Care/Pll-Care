package fullcare.backend.project.dto.request;

import fullcare.backend.global.State;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProjectStateUpdateRequest {

    // TODO Project Id는 필요 없는지?

    // ? NotBlank인가 NotEmpty인가?
    @NotNull
    private State state;
}
