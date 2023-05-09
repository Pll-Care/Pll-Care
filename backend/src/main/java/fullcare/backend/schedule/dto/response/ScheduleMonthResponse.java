package fullcare.backend.schedule.dto.response;

import fullcare.backend.schedule.dto.MeetingDto;
import fullcare.backend.schedule.dto.MilestoneDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduleMonthResponse {
    List<MeetingDto> meetings = new ArrayList<>();
    List<MilestoneDto> milestones = new ArrayList<>();

//    public ScheduleMonthResponse(List<MeetingDto> meetings, List<MilestoneDto> milestones) {
//        this.meetings = meetings;
//        this.milestones = milestones;
//    }
    public void addMeeting(MeetingDto meetingDto){
        meetings.add(meetingDto);
    }
    public void addMilestone(MilestoneDto milestoneDto){
        milestones.add(milestoneDto);
    }
}
