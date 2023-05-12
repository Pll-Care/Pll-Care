package fullcare.backend.schedule.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleDeleteRequest {
    @NotNull
    private Long projectId;
    @NotNull
    private Long scheduleId;
}