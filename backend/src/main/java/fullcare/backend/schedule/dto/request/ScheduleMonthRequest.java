package fullcare.backend.schedule.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleMonthRequest {

    @NotNull
    private Long projectId;

    @NotNull
    @Min(1900)
    @Max(9999)
    private int year;

    @NotNull
    @Min(1)
    @Max(12)
    private int month;
}
