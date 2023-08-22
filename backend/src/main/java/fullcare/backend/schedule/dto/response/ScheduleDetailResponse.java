package fullcare.backend.schedule.dto.response;

import fullcare.backend.schedule.ScheduleCategory;
import fullcare.backend.schedule.dto.DetailMemberDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduleDetailResponse {
    private Long projectId;
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String address;
    private ScheduleCategory scheduleCategory;
    private boolean deleteAuthorization;
    private List<DetailMemberDto> members = new ArrayList<>();
    @Builder
    public ScheduleDetailResponse(Long projectId, String title, String content, LocalDateTime startDate, LocalDateTime endDate, ScheduleCategory scheduleCategory, boolean deleteAuthorization) {
        this.projectId = projectId;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.scheduleCategory = scheduleCategory;
        this.deleteAuthorization = deleteAuthorization;
    }
    public void addMember(DetailMemberDto detailMemberDto){
        members.add(detailMemberDto);
    }
}
