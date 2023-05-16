package fullcare.backend.schedule.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class ScheduleServingResponse {
    private Long id;
    private String name;
    @Builder
    public ScheduleServingResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
