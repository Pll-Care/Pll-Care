package fullcare.backend.schedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScheduleDeleteRequest {
    @NotNull
    private Long projectId;

}
