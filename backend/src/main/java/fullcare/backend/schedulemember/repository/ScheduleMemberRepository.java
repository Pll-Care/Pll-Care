package fullcare.backend.schedulemember.repository;

import fullcare.backend.schedulemember.domain.ScheduleMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember ,Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update schedule_member sc set sc.recentView = :recentView where sc.schedule.id = :scheduleId")
    int updateRecentView(LocalDateTime recentView, Long scheduleId);
}
