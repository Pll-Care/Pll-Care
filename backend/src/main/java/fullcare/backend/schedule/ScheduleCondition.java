package fullcare.backend.schedule;

import fullcare.backend.global.State;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleCondition {
    @NotNull
    private Long projectId;
    @NotNull
    private Long memberId;
    private ScheduleCategory scheduleCategory;
    private boolean isPrevious;


}
