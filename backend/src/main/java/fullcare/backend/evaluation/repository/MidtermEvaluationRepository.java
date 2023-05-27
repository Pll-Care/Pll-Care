package fullcare.backend.evaluation.repository;

import fullcare.backend.evaluation.domain.MidtermEvaluation;
import fullcare.backend.evaluation.dto.MidTermRankProjectionInterface;
import fullcare.backend.evaluation.dto.response.MidtermDetailResponse;
import fullcare.backend.evaluation.dao.BadgeDao;
import fullcare.backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MidtermEvaluationRepository extends JpaRepository<MidtermEvaluation, Long> {
    @Query("select new fullcare.backend.evaluation.dto.response.MidtermDetailResponse(me.evaluationBadge ,count(me.evaluationBadge)) from MidtermEvaluation me where me.voted.id = :memberId and me.project.id = :projectId group by me.evaluationBadge")
    List<MidtermDetailResponse> findAllByMemberId(Long projectId, Long memberId);

    @Query("select new fullcare.backend.evaluation.dao.BadgeDao(me.voted.id, me.voted.name, me.evaluationBadge ,count(me.evaluationBadge)) from MidtermEvaluation me where me.project.id = :projectId and me.voted in :members group by me.evaluationBadge, me.voted.id")
    List<BadgeDao> findList(Long projectId, List<Member> members);

    @Query(value = "select  voted_id as id, count(voted_id) as quantity, rank() over(order by count(voted_id) desc) as ranking from midterm_evaluation where project_id = :projectId group by voted_id", nativeQuery = true)
    List<MidTermRankProjectionInterface> findRank(Long projectId);

    boolean existsByScheduleIdAndVoterId(Long scheduleId, Long voterId);
}
