package fullcare.backend.schedule.dto.response;

import fullcare.backend.schedule.ScheduleCategory;
import fullcare.backend.schedule.domain.Address;
import fullcare.backend.schedule.dto.MemberDto;
import fullcare.backend.schedule.dto.ScheduleDto;
import fullcare.backend.schedule.dto.ScheduleMyDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduleMyListResponse {
    private List<ScheduleMyDto> scheduleMyDtos = new ArrayList<>();
    private ScheduleDto nearSchedule;
}
