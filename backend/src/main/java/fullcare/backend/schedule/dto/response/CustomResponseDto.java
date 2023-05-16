package fullcare.backend.schedule.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CustomResponseDto{
    private LocalDate startDate;
    private LocalDate endDate;
    private List<ScheduleListResponse> schedules = new ArrayList<>();

    public CustomResponseDto(LocalDate startDate, LocalDate endDate, List<ScheduleListResponse> schedules) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.schedules = schedules;
    }
}
