package fullcare.backend.evaluation.service;

import fullcare.backend.evaluation.domain.FinalEvaluation;
import fullcare.backend.evaluation.domain.MidtermEvaluation;
import fullcare.backend.evaluation.dto.request.FinalEvalCreateRequest;
import fullcare.backend.evaluation.dto.request.MidTermEvalCreateRequest;
import fullcare.backend.evaluation.dto.response.FinalEvaluationResponse;
import fullcare.backend.evaluation.repository.FinalEvaluationRepository;
import fullcare.backend.evaluation.repository.MidtermEvaluationRepository;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EvaluationService {

    private final FinalEvaluationRepository finalEvaluationRepository;
    private final MidtermEvaluationRepository midtermEvaluationRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;


    @Transactional
    public void createMidtermEvaluation(MidTermEvalCreateRequest midTermEvalCreateRequest, Member voter) {
        Member voted = memberRepository.findById(midTermEvalCreateRequest.getVotedId()).orElseThrow(() -> new EntityNotFoundException("해당 사용자가 없습니다."));

        MidtermEvaluation newMidtermEvaluation = MidtermEvaluation.createNewMidtermEval()
                .evaluationBadge(midTermEvalCreateRequest.getEvaluationBadge())
                .voter(voter)
                .voted(voted)
                .build();

        midtermEvaluationRepository.save(newMidtermEvaluation);
    }

    @Transactional
    public void createFinalEvaluation(FinalEvalCreateRequest finalEvalCreateRequest, Member evaluator) {
        Member evaluated = memberRepository.findById(finalEvalCreateRequest.getEvaluatedId()).orElseThrow(() -> new EntityNotFoundException("해당 사용자가 없습니다."));
        Project project = projectRepository.findById(finalEvalCreateRequest.getProjectId()).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트가 없습니다."));

        FinalEvaluation newFinalEvaluation = FinalEvaluation.createNewFinalEval()
                .project(project)
                .content(finalEvalCreateRequest.getContent())
                .score(finalEvalCreateRequest.getScore())
                .evaluator(evaluator)
                .evaluated(evaluated)
                .build();

        finalEvaluationRepository.save(newFinalEvaluation);
    }

    // ? 종류별 배지 갯수를 어떻게 count 할 것인가? -> group by 로 하면 될 것 같음.
    public void findMidtermEvaluationDetailResponse(Long memberId) {
        midtermEvaluationRepository.findAllByMemberId(memberId);
    }

    public FinalEvaluationResponse findFinalEvaluationDetailResponse(Long evaluationId) {
        FinalEvaluation findFinalEvaluation = finalEvaluationRepository.findById(evaluationId).orElseThrow(() -> new EntityNotFoundException("해당 최종 평가가 존재하지 않습니다."));
        
        return FinalEvaluationResponse.entityToDto(findFinalEvaluation);
    }

    public void findMidtermEvaluationList(Long projectId) {

    }

    public void findFinalEvaluationList(Long projectId) {

    }
}
