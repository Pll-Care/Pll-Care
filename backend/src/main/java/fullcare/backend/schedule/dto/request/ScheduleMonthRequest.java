package fullcare.backend.schedule.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleMonthRequest {
    @NotNull
    private Long projectId;
    @NotNull
    private int year;
    @NotNull
    private int month;
}
