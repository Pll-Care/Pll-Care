package fullcare.backend.evaluation.repository;

import fullcare.backend.evaluation.domain.FinalTermEvaluation;
import fullcare.backend.evaluation.dao.ScoreDao;
import fullcare.backend.global.State;
import fullcare.backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FinalEvaluationRepository extends JpaRepository<FinalTermEvaluation, Long> {
    @Query("select new fullcare.backend.evaluation.dao.ScoreDao(fe.evaluated.id, avg(fe.score.sincerity),  avg(fe.score.jobPerformance),  avg(fe.score.punctuality),  avg(fe.score.communication)) from FinalTermEvaluation fe where fe.project.id = :projectId and fe.evaluated in :members group by fe.evaluated.id")
    List<ScoreDao> findList(Long projectId, List<Member> members);

    @Query("select new fullcare.backend.evaluation.dao.ScoreDao(fe.project.id, avg(fe.score.sincerity),  avg(fe.score.jobPerformance),  avg(fe.score.punctuality),  avg(fe.score.communication)) from FinalTermEvaluation fe where fe.evaluated.id = :memberId group by fe.evaluated.id, fe.project.id")
    List<ScoreDao> findMyAvgScore(Long memberId);

    boolean existsByIdAndEvaluatorId(Long id, Long evaluatorId);
    boolean existsByIdAndState(Long id, State state);
    boolean existsByEvaluatedIdAndEvaluatorIdAndProjectId(Long evaluatedId, Long evaluatorId,Long projectId);

    List<FinalTermEvaluation> findByProjectIdAndEvaluatedIdAndState(Long projectId, Long evaluatedId, State state);
    List<FinalTermEvaluation> findByProjectIdAndEvaluatorId(Long projectId, Long evaluatorId);

}
