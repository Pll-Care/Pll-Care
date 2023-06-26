package fullcare.backend.schedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ScheduleDetailRequest {

    @NotBlank
    private Long projectId;
}
