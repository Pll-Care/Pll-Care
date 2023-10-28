package fullcare.backend.schedule.dto;

import fullcare.backend.schedule.ScheduleCategory;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class MyScheduleDto {
    private Long projectId;
    private Long scheduleId;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String address;
    private ScheduleCategory scheduleCategory;
    private Boolean check;
    @Builder
    public MyScheduleDto(Long projectId, Long scheduleId, String title, LocalDateTime startDate, LocalDateTime endDate, String address, ScheduleCategory scheduleCategory, Boolean check) {
        this.projectId = projectId;
        this.scheduleId = scheduleId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.address = address;
        this.scheduleCategory = scheduleCategory;
        this.check = check;
    }
}
