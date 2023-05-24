package fullcare.backend.evaluation.repository;

import fullcare.backend.evaluation.domain.FinalTermEvaluation;
import fullcare.backend.evaluation.dto.ScoreDao;
import fullcare.backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FinalEvaluationRepository extends JpaRepository<FinalTermEvaluation, Long> {
    @Query("select new fullcare.backend.evaluation.dto.ScoreDao(fe.evaluated.id, avg(fe.score.sincerity),  avg(fe.score.jobPerformance),  avg(fe.score.punctuality),  avg(fe.score.communication)) from FinalTermEvaluation fe where fe.project.id = :projectId and fe.evaluated in :members group by fe.evaluated.id")
    List<ScoreDao> findList(Long projectId, List<Member> members);
}
