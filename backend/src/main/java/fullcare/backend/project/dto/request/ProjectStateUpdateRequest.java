package fullcare.backend.project.dto.request;

import fullcare.backend.global.State;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProjectStateUpdateRequest {

    // ? NotBlank인가 NotEmpty인가?
    @NotNull
    private State state;
}
