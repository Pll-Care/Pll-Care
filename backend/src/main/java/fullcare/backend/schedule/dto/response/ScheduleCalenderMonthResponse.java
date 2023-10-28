package fullcare.backend.schedule.dto.response;

import fullcare.backend.schedule.dto.MeetingDto;
import fullcare.backend.schedule.dto.MilestoneDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduleCalenderMonthResponse {
    List<MeetingDto> meetings = new ArrayList<>();
    List<MilestoneDto> milestones = new ArrayList<>();
    public void addMeeting(MeetingDto meetingDto){
        meetings.add(meetingDto);
    }
    public void addMilestone(MilestoneDto milestoneDto){
        milestones.add(milestoneDto);
    }
}
