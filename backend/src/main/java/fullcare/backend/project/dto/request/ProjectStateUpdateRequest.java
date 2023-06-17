package fullcare.backend.project.dto.request;

import fullcare.backend.global.State;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Getter
public class ProjectStateUpdateRequest {
    @NotNull
    private State state;
}
