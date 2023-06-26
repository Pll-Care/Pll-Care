package fullcare.backend.schedule.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ScheduleMonthRequest {

    @NotBlank
    private Long projectId;

    @NotBlank
    @Min(1900)
    @Max(9999)
    private int year;

    @NotBlank
    @Min(1)
    @Max(12)
    private int month;
}
