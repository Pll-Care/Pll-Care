package fullcare.backend.evaluation.repository;

import fullcare.backend.evaluation.domain.FinalTermEvaluation;
import fullcare.backend.evaluation.dao.ScoreDao;
import fullcare.backend.projectmember.domain.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FinalEvaluationRepository extends JpaRepository<FinalTermEvaluation, Long> {
    @Query("select new fullcare.backend.evaluation.dao.ScoreDao(fe.evaluated.member.id, avg(fe.score.sincerity),  avg(fe.score.jobPerformance),  avg(fe.score.punctuality),  avg(fe.score.communication)) from FinalTermEvaluation fe where fe.project.id = :projectId and fe.evaluated in :members group by fe.evaluated.id")
    List<ScoreDao> findList(Long projectId, List<ProjectMember> members);

    @Query("select new fullcare.backend.evaluation.dao.ScoreDao(fe.project.id, avg(fe.score.sincerity),  avg(fe.score.jobPerformance),  avg(fe.score.punctuality),  avg(fe.score.communication)) from FinalTermEvaluation fe where fe.evaluated.member.id = :memberId group by fe.evaluated.id, fe.project.id")
    List<ScoreDao> findMyAvgScore(Long memberId);

    boolean existsByIdAndEvaluatorId(Long id, Long evaluatorId);
    boolean existsById(Long id);
    boolean existsByEvaluatedIdAndEvaluatorIdAndProjectId(Long evaluatedId, Long evaluatorId,Long projectId);

    List<FinalTermEvaluation> findByProjectIdAndEvaluatedId(Long projectId, Long evaluatedId);
    List<FinalTermEvaluation> findByProjectIdAndEvaluatorId(Long projectId, Long evaluatorId);

    @Query("select fe from FinalTermEvaluation fe join fe.evaluated.member m where fe.project.id in :projectIds and m.id = :evaluatedId")
    List<FinalTermEvaluation> findByProjectIdsAndEvaluatedId(List<Long> projectIds, Long evaluatedId);
}
