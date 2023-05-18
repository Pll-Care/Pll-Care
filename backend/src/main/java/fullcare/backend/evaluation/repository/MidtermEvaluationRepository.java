package fullcare.backend.evaluation.repository;

import fullcare.backend.evaluation.domain.MidtermEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MidtermEvaluationRepository extends JpaRepository<MidtermEvaluation, Long> {
    @Query("select me from MidtermEvaluation me join fetch me.voted vtd where vtd.id = :memberId group by me.evaluationBadge")
    void findAllByMemberId(Long memberId);
}
