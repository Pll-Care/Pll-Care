package fullcare.backend.schedule.repository;

import fullcare.backend.global.State;
import fullcare.backend.schedule.domain.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("select s from schedule s where s.project.id = :projectId and type(s) in (Milestone) order by s.startDate")
    List<Schedule> findMileStoneByProjectId(@Param("projectId") Long projectId);

    @Query(value = "select s from schedule s join fetch s.scheduleMembers sm join fetch sm.projectMember " +
            "where s.project.id = :projectId " +
            "and ((s.startDate between :startCheckStartDate and :startCheckEndDate and s.endDate between :startCheckStartDate and :startCheckEndDate) or (:startCheckStartDate between s.startDate and s.endDate))" +
            "order by s.startDate")
    List<Schedule> findDaily(@Param("projectId") Long projectId, LocalDateTime startCheckStartDate, LocalDateTime startCheckEndDate);

    @Query("select s from schedule s join fetch s.scheduleMembers sm where s.id = :scheduleId")
    Optional<Schedule> findJoinSMById(@Param("scheduleId") Long scheduleId);

    @Query("select s from schedule s join fetch s.scheduleMembers sm join fetch sm.projectMember where s.id = :scheduleId")
    Optional<Schedule> findJoinSMJoinMemberById(@Param("scheduleId") Long scheduleId);

    List<Schedule> findByProjectId(Long projectId);

    Optional<Schedule> findByIdAndState(Long scheduleId, State state);
}
