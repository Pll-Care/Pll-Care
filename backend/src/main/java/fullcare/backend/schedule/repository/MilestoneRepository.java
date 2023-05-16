package fullcare.backend.schedule.repository;

import fullcare.backend.schedule.domain.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
}
