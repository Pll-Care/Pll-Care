package fullcare.backend.schedule.repository;

import fullcare.backend.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("select s from schedule s where s.project.id = :projectId and type(s) in (Meeting)")
    List<Schedule> findMeetingByProjectId(@Param("projectId") Long projectId);
    @Query("select s from schedule s where s.project.id = :projectId and type(s) in (Milestone)")
    List<Schedule> findMileStoneByProjectId(@Param("projectId") Long projectId);

    List<Schedule> findByStartDateBetween(LocalDateTime now, LocalDateTime end);
}
