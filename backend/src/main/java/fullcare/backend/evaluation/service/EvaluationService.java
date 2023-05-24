package fullcare.backend.evaluation.service;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import fullcare.backend.evaluation.domain.FinalTermEvaluation;
import fullcare.backend.evaluation.domain.MidtermEvaluation;
import fullcare.backend.evaluation.dto.BadgeDto;
import fullcare.backend.evaluation.dto.MemberDto;
import fullcare.backend.evaluation.dto.request.FinalEvalCreateRequest;
import fullcare.backend.evaluation.dto.request.MidTermEvalCreateRequest;
import fullcare.backend.evaluation.dto.response.FinalEvaluationResponse;
import fullcare.backend.evaluation.dto.response.MidTermEvalListResponse;
import fullcare.backend.evaluation.dto.response.MidTermEvalModalResponse;
import fullcare.backend.evaluation.dto.response.MidtermDetailResponse;
import fullcare.backend.evaluation.dto.MidtermBadgeListDao;
import fullcare.backend.evaluation.repository.FinalEvaluationRepository;
import fullcare.backend.evaluation.repository.MidtermEvaluationRepository;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.schedule.domain.Schedule;
import fullcare.backend.schedule.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EvaluationService {

    private final FinalEvaluationRepository finalEvaluationRepository;
    private final MidtermEvaluationRepository midtermEvaluationRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void createMidtermEvaluation(MidTermEvalCreateRequest midTermEvalCreateRequest, Member voter) {
        Member voted = memberRepository.findById(midTermEvalCreateRequest.getVotedId()).orElseThrow(() -> new EntityNotFoundException("해당 사용자가 없습니다."));
//        if(voter.getId() == voted.getId()){
//            throw new RuntimeException("자신의 평가는 불가능합니다.");
//        }//한 스케쥴에 하나의 평가만 가능
        Schedule schedule = scheduleRepository.findById(midTermEvalCreateRequest.getScheduleId()).orElseThrow();
        Project project = projectRepository.findById(midTermEvalCreateRequest.getProjectId()).orElseThrow();
        MidtermEvaluation newMidtermEvaluation = MidtermEvaluation.createNewMidtermEval()
                .evaluationBadge(midTermEvalCreateRequest.getEvaluationBadge())
                .voter(voter)
                .voted(voted)
                .project(project)
                .schedule(schedule)
                .build();

        midtermEvaluationRepository.save(newMidtermEvaluation);
    }

    @Transactional
    public void createFinalEvaluation(FinalEvalCreateRequest finalEvalCreateRequest, Member evaluator) {
        Member evaluated = memberRepository.findById(finalEvalCreateRequest.getEvaluatedId()).orElseThrow(() -> new EntityNotFoundException("해당 사용자가 없습니다."));
        Project project = projectRepository.findById(finalEvalCreateRequest.getProjectId()).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트가 없습니다."));
        if(evaluator.getId() == evaluated.getId()){
            throw new RuntimeException("자신의 평가는 불가능합니다.");
        }

        FinalTermEvaluation newFinalTermEvaluation = FinalTermEvaluation.createNewFinalEval()
                .project(project)
                .content(finalEvalCreateRequest.getContent())
                .score(finalEvalCreateRequest.getScore())
                .evaluator(evaluator)
                .evaluated(evaluated)
                .build();

        finalEvaluationRepository.save(newFinalTermEvaluation);
    }

    // ? 종류별 배지 갯수를 어떻게 count 할 것인가? -> group by 로 하면 될 것 같음.
    public List<MidtermDetailResponse> findMidtermEvaluationDetailResponse(Long projectId, Long memberId) {
        List<MidtermDetailResponse> midtermDetailResponses = midtermEvaluationRepository.findAllByMemberId(projectId, memberId);
        setBadges(midtermDetailResponses);

        return  midtermDetailResponses;
    }

    private static void setBadges(List<MidtermDetailResponse> midtermDetailResponses) {
        List<EvaluationBadge> evaluationBadges = new ArrayList<>();
        evaluationBadges.add(EvaluationBadge.탁월한_리더);
        evaluationBadges.add(EvaluationBadge.열정적인_참여자);
        evaluationBadges.add(EvaluationBadge.최고의_서포터);
        evaluationBadges.add(EvaluationBadge.아이디어_뱅크);
        for (MidtermDetailResponse midtermDetailResponse : midtermDetailResponses) {
            if(midtermDetailResponse.getEvaluationBadge().equals(EvaluationBadge.탁월한_리더)){
                evaluationBadges.remove(EvaluationBadge.탁월한_리더);
            }else if(midtermDetailResponse.getEvaluationBadge().equals(EvaluationBadge.열정적인_참여자)){
                evaluationBadges.remove(EvaluationBadge.열정적인_참여자);
            }else if(midtermDetailResponse.getEvaluationBadge().equals(EvaluationBadge.최고의_서포터)){
                evaluationBadges.remove(EvaluationBadge.최고의_서포터);
            }else if(midtermDetailResponse.getEvaluationBadge().equals(EvaluationBadge.아이디어_뱅크)){
                evaluationBadges.remove(EvaluationBadge.아이디어_뱅크);
            }
        }
        for (EvaluationBadge evaluationBadge : evaluationBadges) { // 없는 뱃지 추가
            midtermDetailResponses.add(new MidtermDetailResponse(evaluationBadge,0l));
        }
    }

    public FinalEvaluationResponse findFinalEvaluationDetailResponse(Long evaluationId) {
        FinalTermEvaluation findFinalTermEvaluation = finalEvaluationRepository.findById(evaluationId).orElseThrow(() -> new EntityNotFoundException("해당 최종 평가가 존재하지 않습니다."));
        
        return FinalEvaluationResponse.entityToDto(findFinalTermEvaluation);
    }

    public List<MidTermEvalListResponse> findMidtermEvaluationList(Long projectId) {
        Project project = projectRepository.findJoinPMJoinMemberById(projectId).orElseThrow();
        List<Member> members = project.getProjectMembers().stream().map(pm -> pm.getMember()).collect(Collectors.toList());
        List<MidtermBadgeListDao> midtermBadgeListRespons = midtermEvaluationRepository.findList(projectId, members);
        MidTermEvalListResponse midTermEvalList = null;
        List<MidTermEvalListResponse> midTermEvalListResponse = new ArrayList<>();
        boolean flag = false;
        for (MidtermBadgeListDao midtermBadgeListDao : midtermBadgeListRespons) {
            if(midTermEvalListResponse.size() == 0){
                midTermEvalList = MidTermEvalListResponse.builder().memberId(midtermBadgeListDao.getMemberId()).name(midtermBadgeListDao.getName()).imageUrl(null).build();
                midTermEvalListResponse.add(midTermEvalList);
                flag = true;
            }else if(midTermEvalListResponse.get(midTermEvalListResponse.size()-1).getMemberId() != midtermBadgeListDao.getMemberId()){
                System.out.println("리스트 끝 memberId = " + midTermEvalListResponse.get(midTermEvalListResponse.size()-1).getMemberId());
                System.out.println("현재 getMemberId() = " + midtermBadgeListDao.getMemberId());
                if(flag){flag = false;}//첫 사람 평가 중복 추가 방지
                else{midTermEvalListResponse.add(midTermEvalList);}
                midTermEvalList = MidTermEvalListResponse.builder().memberId(midtermBadgeListDao.getMemberId()).name(midtermBadgeListDao.getName()).imageUrl(null).build();
            }
            midTermEvalList.addBadge(new BadgeDto(midtermBadgeListDao.getEvaluationBadge(),midtermBadgeListDao.getQuantity(),null));


            System.out.println("midtermListResponse = " + midtermBadgeListDao);
        }// JSON 응답으로 넘겨줄 때 없는 뱃지 추가 + 멤버별로 틀 맞춰서 깔끔하게 수정 필요, 프로필 이미지 추가
        midTermEvalListResponse.add(midTermEvalList);// 마지막 추가
        return midTermEvalListResponse;
    }

    public void findFinalEvaluationList(Long projectId) {

    }

    public MidTermEvalModalResponse modal(Long scheduleId, Long memberId) {
        Schedule schedule = scheduleRepository.findJoinSMJoinMemberById(scheduleId).orElseThrow();
        Member member = memberRepository.findById(memberId).orElseThrow();
        List<MemberDto> memberDto = schedule.getScheduleMembers().stream().filter(sm -> sm.getMember() != member).map(sm -> MemberDto.builder().member(sm.getMember()).build()).collect(Collectors.toList());// 나를 제외한 일정에 참여한 팀원


        MidTermEvalModalResponse midTermEvalModalResponse = MidTermEvalModalResponse.builder()
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .state(schedule.getState())
                .build();
        midTermEvalModalResponse.setMembers(memberDto);
        List<BadgeDto> badgeDtos = new ArrayList<>();
        badgeDtos.add(new BadgeDto(EvaluationBadge.열정적인_참여자, null));
        badgeDtos.add(new BadgeDto(EvaluationBadge.아이디어_뱅크, null));
        badgeDtos.add(new BadgeDto(EvaluationBadge.탁월한_리더, null));
        badgeDtos.add(new BadgeDto(EvaluationBadge.최고의_서포터, null));
        midTermEvalModalResponse.setBadges(badgeDtos);

        return midTermEvalModalResponse;
    }
}
