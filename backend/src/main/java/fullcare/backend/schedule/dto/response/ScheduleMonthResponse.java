package fullcare.backend.schedule.dto.response;

import fullcare.backend.global.State;
import fullcare.backend.member.domain.Member;
import fullcare.backend.schedule.ScheduleCategory;
import fullcare.backend.schedule.domain.Address;
import fullcare.backend.schedule.dto.MemberDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduleMonthResponse {
    private Long scheduleId;
    private String title;
//    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    //private Address address;
    private ScheduleCategory scheduleCategory;
    private List<MemberDto> members = new ArrayList<>();
    private State state;
    private LocalDate modifyDate;
    private Boolean check;
    @Builder
    public ScheduleMonthResponse(Long scheduleId, String title, LocalDateTime startDate, LocalDateTime endDate, ScheduleCategory scheduleCategory, LocalDate modifyDate , State state) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
        this.modifyDate = modifyDate;
        //this.address = address;
        this.scheduleCategory = scheduleCategory;
    }

    public void addMember(Member member){
        members.add(MemberDto.builder()
                .id(member.getId())
                .name(member.getName()).build());
    }
    public void updateCheck(boolean check){
        this.check = check;
    }
}
