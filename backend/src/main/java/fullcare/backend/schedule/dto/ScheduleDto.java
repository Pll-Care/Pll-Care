package fullcare.backend.schedule.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleDto {
    private Long projectId;
    private Long scheduleId;
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String address;
    @Builder
    public ScheduleDto(Long projectId, Long scheduleId, String title, String content, LocalDateTime startDate, LocalDateTime endDate, String address) {
        this.projectId = projectId;
        this.scheduleId = scheduleId;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.address = address;
    }
}
