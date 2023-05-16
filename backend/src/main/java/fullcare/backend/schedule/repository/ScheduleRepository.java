package fullcare.backend.schedule.repository;

import fullcare.backend.schedule.domain.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    @Query("select s from schedule s where type(s) in (Milestone)  and (s.startDate between :startDate and :endDate or s.endDate between :startDate and :endDate)")
    List<Schedule> findMeetingCalender(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    Page<Schedule> findMonthByStartDateBetweenOrEndDateBetween(Pageable pageable, LocalDateTime startCheckStartDate, LocalDateTime startCheckEndDate, LocalDateTime endCheckStartDate, LocalDateTime endCheckEndDate);
    @Query("select s from schedule s join fetch s.scheduleMembers sm where sm.member.id = :memberId and (s.startDate between :startDate and :endDate or s.endDate between :startDate and :endDate)")
    List<Schedule> findMonthListByMember(@Param("memberId")Long memberId, @Param("startDate")LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    @Modifying
    @Query("update schedule s set s.state = '진행중' where s.startDate < :now and s.state = '예정' and s.project.id = :projectId")
    int updateStateContinue(@Param("now") LocalDateTime now, @Param("projectId") Long projectId);

}
