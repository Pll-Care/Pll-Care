package fullcare.backend.evaluation.service;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import fullcare.backend.evaluation.domain.FinalTermEvaluation;
import fullcare.backend.evaluation.domain.MidtermEvaluation;
import fullcare.backend.evaluation.domain.Score;
import fullcare.backend.evaluation.dto.*;
import fullcare.backend.evaluation.dto.request.FinalEvalCreateRequest;
import fullcare.backend.evaluation.dto.request.MidTermEvalCreateRequest;
import fullcare.backend.evaluation.dto.response.FinalEvaluationResponse;
import fullcare.backend.evaluation.dto.ChartDto;
import fullcare.backend.evaluation.dto.response.EverythingEvalResponse;
import fullcare.backend.evaluation.dto.response.MidTermEvalModalResponse;
import fullcare.backend.evaluation.dto.response.MidtermDetailResponse;
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

import java.util.*;
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
    public void createFinalEvaluation(FinalEvalCreateRequest finalEvalCreateRequest, Member evaluator) { //점수 5점 이상일 경우 에러처리 필요
        Member evaluated = memberRepository.findById(finalEvalCreateRequest.getEvaluatedId()).orElseThrow(() -> new EntityNotFoundException("해당 사용자가 없습니다."));
        Project project = projectRepository.findById(finalEvalCreateRequest.getProjectId()).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트가 없습니다."));
//        if(evaluator.getId() == evaluated.getId()){
//            throw new RuntimeException("자신의 평가는 불가능합니다.");
//        }

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
//        Badge.setBadges(Collections.unmodifiableList(midtermDetailResponses));

        return  midtermDetailResponses;
    }


    public FinalEvaluationResponse findFinalEvaluationDetailResponse(Long evaluationId) {
        FinalTermEvaluation findFinalTermEvaluation = finalEvaluationRepository.findById(evaluationId).orElseThrow(() -> new EntityNotFoundException("해당 최종 평가가 존재하지 않습니다."));
        
        return FinalEvaluationResponse.entityToDto(findFinalTermEvaluation);
    }

    public EverythingEvalResponse findMidtermEvaluationList(Long projectId) {
        Project project = projectRepository.findJoinPMJoinMemberById(projectId).orElseThrow();
        List<Member> members = project.getProjectMembers().stream().map(pm -> pm.getMember()).collect(Collectors.toList());
        List<MidtermBadgeListDao> midtermBadgeList = midtermEvaluationRepository.findList(projectId, members);
        List<MidTermRankProjectionInterface> rank = midtermEvaluationRepository.findRank(projectId);

        List<ChartDto> midTermEvalChartDto = members.stream().map(m -> ChartDto.builder()
                .memberId(m.getId())
                .name(m.getName())
//                .imageUrl(null)
                .build()
        ).collect(Collectors.toList());
        for (MidtermBadgeListDao badgeDao : midtermBadgeList) {
            for (ChartDto chart : midTermEvalChartDto) {
                if(badgeDao.getMemberId() == chart.getMemberId()){
                    chart.addEvaluation(new BadgeDto(badgeDao.getEvaluationBadge(), badgeDao.getQuantity(), null));
                }
            }
        }

        List<MidTermRankingDto> rankingDtos = new ArrayList<>();// 랭킹 부분
        for (MidTermRankProjectionInterface r : rank) {
            String name = members.stream().filter(m -> m.getId() == r.getQuantity()).map(m -> m.getName()).findFirst().get();
            rankingDtos.add(MidTermRankingDto.builder()
                    .rank(r.getRanking())
                    .memberId(r.getId())
                    .name(name)
                    .quantity(r.getQuantity())
                    .build()
            );
        }

        for (ChartDto chartDto : midTermEvalChartDto) {
//            System.out.println("m.getBadges() = " + chartDto.getBadges());
            setBadge(chartDto.getEvaluation());
        }
        EverythingEvalResponse everythingEvalResponse = new EverythingEvalResponse(midTermEvalChartDto, rankingDtos);
        return everythingEvalResponse;
    }

    public EverythingEvalResponse findFinalEvaluationList(Long projectId) {
        Project project = projectRepository.findJoinPMJoinMemberById(projectId).orElseThrow();
        List<Member> members = project.getProjectMembers().stream().map(pm -> pm.getMember()).collect(Collectors.toList());
        List<ScoreDao> scoreDaos = finalEvaluationRepository.findList(projectId, members);

        List<ChartDto> finalTermEvalChartDto = members.stream().map(m -> ChartDto.builder()
                .memberId(m.getId())
                .name(m.getName())
//                .imageUrl(null)
                .build()
        ).collect(Collectors.toList());
        for (ScoreDao s : scoreDaos) {
            for (ChartDto chart : finalTermEvalChartDto) {
                if(s.getMemberId() == chart.getMemberId()){
                    chart.addEvaluation(ScoreDto.builder()
                            .jobPerformance(s.getJobPerformance())
                            .punctuality(s.getPunctuality())
                            .communication(s.getCommunication())
                            .sincerity(s.getSincerity()).build());
                }
            }
        }
        finalTermEvalChartDto.stream().filter(fe->fe.getEvaluation().size()==0).forEach(fe->fe.addEvaluation(new ScoreDto()));

        //랭킹 부분
        List<FinalTermRankingDto> rankingDtos = finalTermEvalChartDto.stream().map(fe -> FinalTermRankingDto.builder()
                .memberId(fe.getMemberId())
                .name(fe.getName())
                .score(Score.avg((ScoreDto) fe.getEvaluation().get(0)))
                .build()).collect(Collectors.toList());
        Collections.sort(rankingDtos, (a, b) -> (int)Math.round(b.getScore() - a.getScore())); // 정렬 검증 필요
        List<FinalTermRankingDto> ranks = rankingDtos.stream().filter(r -> r.getScore() != 0).collect(Collectors.toList());// 0점 랭킹에서 제거

        for (FinalTermRankingDto exRank : rankingDtos) {
            Long rank = 1l;
            for (FinalTermRankingDto inRank : rankingDtos) {
                if(exRank.getScore()<inRank.getScore()){
                    rank++;
                }
            }
            exRank.setRank(rank);
        }

        EverythingEvalResponse everythingEvalResponse = new EverythingEvalResponse(finalTermEvalChartDto, ranks);
        return everythingEvalResponse;
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
//        badgeDtos.add(new BadgeDto(EvaluationBadge.열정적인_참여자, null));
//        badgeDtos.add(new BadgeDto(EvaluationBadge.아이디어_뱅크, null));
//        badgeDtos.add(new BadgeDto(EvaluationBadge.탁월한_리더, null));
//        badgeDtos.add(new BadgeDto(EvaluationBadge.최고의_서포터, null));
        setBadge(badgeDtos);
        midTermEvalModalResponse.setBadges(badgeDtos);
        return midTermEvalModalResponse;
    }


    ////////////////////////////////////////////////////////////////////////////////
    // 하나로 통합 필요
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

    private static void setBadge(List<BadgeDto> badgeDtos) {
        List<EvaluationBadge> evaluationBadges = new ArrayList<>();
        evaluationBadges.add(EvaluationBadge.탁월한_리더);
        evaluationBadges.add(EvaluationBadge.열정적인_참여자);
        evaluationBadges.add(EvaluationBadge.최고의_서포터);
        evaluationBadges.add(EvaluationBadge.아이디어_뱅크);
        for (BadgeDto badgeDto : badgeDtos) {
            if(badgeDto.getEvaluationBadge().equals(EvaluationBadge.탁월한_리더)){
                evaluationBadges.remove(EvaluationBadge.탁월한_리더);
            }else if(badgeDto.getEvaluationBadge().equals(EvaluationBadge.열정적인_참여자)){
                evaluationBadges.remove(EvaluationBadge.열정적인_참여자);
            }else if(badgeDto.getEvaluationBadge().equals(EvaluationBadge.최고의_서포터)){
                evaluationBadges.remove(EvaluationBadge.최고의_서포터);
            }else if(badgeDto.getEvaluationBadge().equals(EvaluationBadge.아이디어_뱅크)){
                evaluationBadges.remove(EvaluationBadge.아이디어_뱅크);
            }
        }
        for (EvaluationBadge evaluationBadge : evaluationBadges) { // 없는 뱃지 추가
            badgeDtos.add(new BadgeDto(evaluationBadge,null));
        }
    }
////////////////////////////////////////////////////////////////////////////////
}
