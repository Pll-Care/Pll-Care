package fullcare.backend.evaluation.repository;

import fullcare.backend.evaluation.domain.FinalEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinalEvaluationRepository extends JpaRepository<FinalEvaluation, Long> {
}
