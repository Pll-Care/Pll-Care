package fullcare.backend.schedule.repository;

import fullcare.backend.schedule.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
