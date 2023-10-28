package fullcare.backend.schedule.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleDto {
    private Long id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long order;

    @Builder
    public ScheduleDto(Long id, String title, LocalDateTime startDate, LocalDateTime endDate, Long order) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.order = order;
    }

}
