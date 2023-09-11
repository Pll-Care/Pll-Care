package fullcare.backend.schedulemember.repository;

import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.schedulemember.domain.ScheduleMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember ,Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update schedule_member sc set sc.recentView = :recentView where sc.schedule.id = :scheduleId")
    int updateRecentView(LocalDateTime recentView, Long scheduleId);

    @Query("select sm from schedule_member sm where sm.schedule.id = :scheduleId and sm.projectMember.id = :pmId")
    Optional<ScheduleMember> findByScheduleIdAndPmId(@Param("scheduleId") Long scheduleId, @Param("pmId") Long pmId);
}
