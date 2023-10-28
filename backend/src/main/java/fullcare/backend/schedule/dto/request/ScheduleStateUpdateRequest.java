package fullcare.backend.schedule.dto.request;

import fullcare.backend.global.State;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleStateUpdateRequest {

    @NotNull
    private Long projectId;

    // ? NotBlank인가 NotEmpty인가?
    @NotNull
    private State state;
}
