package fullcare.backend.schedule.dto.response;

import fullcare.backend.schedule.DateCategory;
import fullcare.backend.schedule.dto.ScheduleDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class OverallScheduleResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private DateCategory dateCategory;
    private List<ScheduleDto> schedules = new ArrayList<>();

    public OverallScheduleResponse(LocalDate startDate, LocalDate endDate, DateCategory dateCategory, List<ScheduleDto> schedules) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateCategory = dateCategory;
        this.schedules = schedules;
    }
}
