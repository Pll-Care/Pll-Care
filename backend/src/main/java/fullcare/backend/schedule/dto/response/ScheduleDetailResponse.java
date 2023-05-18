package fullcare.backend.schedule.dto.response;

import fullcare.backend.member.domain.Member;
import fullcare.backend.schedule.ScheduleCategory;
import fullcare.backend.schedule.domain.Address;
import fullcare.backend.schedule.dto.DetailMemberDto;
import fullcare.backend.schedule.dto.MemberDto;
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
    private Address address;
    private ScheduleCategory scheduleCategory;
    private List<DetailMemberDto> members = new ArrayList<>();
    @Builder
    public ScheduleDetailResponse(Long projectId, String title, String content, LocalDateTime startDate, LocalDateTime endDate, ScheduleCategory scheduleCategory) {
        this.projectId = projectId;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.scheduleCategory = scheduleCategory;
    }
    public void addMember(DetailMemberDto detailMemberDto){
        members.add(detailMemberDto);
    }
}
