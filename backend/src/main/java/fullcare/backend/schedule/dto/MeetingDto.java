package fullcare.backend.schedule.dto;

import fullcare.backend.schedule.domain.Address;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeetingDto {
    private Long scheduleId;
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Address address;
    @Builder
    public MeetingDto(Long scheduleId, String title, String content, LocalDateTime startDate, LocalDateTime endDate, Address address) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.address = address;
    }
}
