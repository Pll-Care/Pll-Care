package fullcare.backend.evaluation.repository;

import fullcare.backend.evaluation.domain.MidtermEvaluation;
import fullcare.backend.evaluation.dto.BadgeDto;
import fullcare.backend.evaluation.dto.MidTermRankProjectionInterface;
import fullcare.backend.evaluation.dto.response.MidtermDetailResponse;
import fullcare.backend.evaluation.dao.BadgeDao;
import fullcare.backend.member.domain.Member;
import fullcare.backend.projectmember.domain.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MidtermEvaluationRepository extends JpaRepository<MidtermEvaluation, Long> {
    @Query("select new fullcare.backend.evaluation.dto.BadgeDto(me.evaluationBadge ,count(me.evaluationBadge)) from MidtermEvaluation me where me.voted.id = :pmId and me.project.id = :projectId group by me.evaluationBadge")
    List<BadgeDto> findAllByMemberId(Long projectId, Long pmId);

    @Query("select new fullcare.backend.evaluation.dao.BadgeDao(vm.id, vm.name, me.evaluationBadge ,count(me.evaluationBadge)) from MidtermEvaluation me join me.voted v join v.member vm where me.project.id = :projectId and me.voted in :members group by me.evaluationBadge, me.voted.id")
    List<BadgeDao> findList(Long projectId, List<ProjectMember> members);

    @Query(value = "select  voted_id as id, count(voted_id) as quantity, rank() over(order by count(voted_id) desc) as ranking from midterm_evaluation where project_id = :projectId group by voted_id", nativeQuery = true)
    List<MidTermRankProjectionInterface> findRank(Long projectId);

    boolean existsByScheduleIdAndVoterId(Long scheduleId, Long voterId);

    List<MidtermEvaluation> findByProjectId(Long projectId);
}
