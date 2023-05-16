package fullcare.backend.schedule.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleMonthListRequest {
    @NotNull
    private int year;
    @NotNull
    private int month;
    @NotNull
    private Long projectId;
    @NotNull
    private Long memberId;
}
