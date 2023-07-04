package fullcare.backend.schedule.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleListResponse {
    private Long scheduleId;
    private String title;
    //private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long order;
    @Builder
    public ScheduleListResponse(Long scheduleId, String title,LocalDateTime startDate,LocalDateTime endDate, Long order) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.order = order;
    }

    public ScheduleListResponse(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
