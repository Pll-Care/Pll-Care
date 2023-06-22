package fullcare.backend.schedule.repository;

import fullcare.backend.schedule.ScheduleCondition;
import fullcare.backend.schedule.domain.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScheduleRepositoryCustom {
    List<Schedule> search(ScheduleCondition scheduleCondition, Long projectId);
}
