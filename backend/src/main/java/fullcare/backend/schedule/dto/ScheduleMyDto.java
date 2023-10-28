package fullcare.backend.schedule.dto;

import fullcare.backend.schedule.ScheduleCategory;
import fullcare.backend.schedule.domain.Address;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ScheduleMyDto {
    private Long projectId;
    private Long scheduleId;
    private String title;
    //    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Address address;
    private ScheduleCategory scheduleCategory;
    //    private List<MemberDto> members = new ArrayList<>();
    private Boolean check;
    @Builder
    public ScheduleMyDto(Long projectId, Long scheduleId, String title, LocalDateTime startDate, LocalDateTime endDate, Address address, ScheduleCategory scheduleCategory, Boolean check) {
        this.projectId = projectId;
        this.scheduleId = scheduleId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.address = address;
        this.scheduleCategory = scheduleCategory;
        this.check = check;
    }
    public void updateCheck(boolean check){
        this.check = check;
    }
}
