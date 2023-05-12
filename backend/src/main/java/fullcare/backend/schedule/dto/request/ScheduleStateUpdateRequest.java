package fullcare.backend.schedule.dto.request;

import fullcare.backend.global.State;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleStateUpdateRequest {
    @NotNull
    private Long projectId;
    @NotNull
    private Long scheduleId;
    @NotNull
    private State state;
}
