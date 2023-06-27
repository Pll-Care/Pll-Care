package fullcare.backend.schedule.dto.response;


import fullcare.backend.schedule.dto.ScheduleDto;
import fullcare.backend.schedule.dto.ScheduleMyDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduleMyListResponse {
    private List<ScheduleMyDto> scheduleMyDtos = new ArrayList<>();
    private ScheduleDto nearSchedule;
}
