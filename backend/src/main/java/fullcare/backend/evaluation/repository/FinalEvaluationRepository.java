package fullcare.backend.evaluation.repository;

import fullcare.backend.evaluation.domain.FinalTermEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinalEvaluationRepository extends JpaRepository<FinalTermEvaluation, Long> {
}
