package fullcare.backend.schedule.dto;

import fullcare.backend.schedule.domain.Address;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MilestoneDto {
    private Long scheduleId;
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @Builder
    public MilestoneDto(Long scheduleId, String title, String content, LocalDateTime startDate, LocalDateTime endDate) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
