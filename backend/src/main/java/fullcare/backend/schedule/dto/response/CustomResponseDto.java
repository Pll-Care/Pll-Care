package fullcare.backend.schedule.dto.response;

import fullcare.backend.schedule.DateCategory;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CustomResponseDto{
    private LocalDate startDate;
    private LocalDate endDate;
    private DateCategory dateCategory;
    private List<ScheduleListResponse> schedules = new ArrayList<>();

    public CustomResponseDto(LocalDate startDate, LocalDate endDate, DateCategory dateCategory, List<ScheduleListResponse> schedules) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateCategory = dateCategory;
        this.schedules = schedules;
    }
}
