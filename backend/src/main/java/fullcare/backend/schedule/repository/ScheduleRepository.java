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

    List<Schedule> findByStartDateBetweenOrEndDateBetween(LocalDateTime startCheckStartDate, LocalDateTime startCheckEndDate, LocalDateTime endCheckStartDate, LocalDateTime endCheckEndDate);
    @Query("select s from schedule s join s.scheduleMembers sm where sm.member.id = :memberId and (s.startDate between :startDate and :endDate or s.endDate between :startDate and :endDate)")
    List<Schedule> findMonthListByMember(@Param("memberId")Long memberId, @Param("startDate")LocalDateTime startCheckStartDate, @Param("endDate") LocalDateTime startCheckEndDate);
}