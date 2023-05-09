package fullcare.backend.schedule.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleListResponse {
    private Long scheduleId;
    private String title;
    private String content;
    private LocalDateTime startDate;
    private String category;
    @Builder
    public ScheduleListResponse(Long scheduleId, String title, String content, LocalDateTime startDate, String category) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.category = category;
    }

    public ScheduleListResponse(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
