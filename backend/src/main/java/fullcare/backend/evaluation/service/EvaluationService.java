package fullcare.backend.evaluation.service;


import fullcare.backend.evaluation.dao.BadgeDao;
import fullcare.backend.evaluation.dao.ScoreDao;
import fullcare.backend.evaluation.domain.EvaluationBadge;
import fullcare.backend.evaluation.domain.FinalTermEvaluation;
import fullcare.backend.evaluation.domain.MidtermEvaluation;
import fullcare.backend.evaluation.domain.Score;
import fullcare.backend.evaluation.dto.*;
import fullcare.backend.evaluation.dto.request.FinalEvalCreateRequest;
import fullcare.backend.evaluation.dto.request.FinalEvalUpdateRequest;
import fullcare.backend.evaluation.dto.request.MidTermEvalCreateRequest;
import fullcare.backend.evaluation.dto.response.*;
import fullcare.backend.evaluation.repository.FinalEvaluationRepository;
import fullcare.backend.evaluation.repository.MidtermEvaluationRepository;
import fullcare.backend.global.State;
import fullcare.backend.global.errorcode.EvaluationErrorCode;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.errorcode.ScheduleErrorCode;
import fullcare.backend.global.exceptionhandling.exception.*;
import fullcare.backend.member.domain.Member;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.project.service.ProjectService;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import fullcare.backend.schedule.domain.Schedule;
import fullcare.backend.schedule.repository.ScheduleRepository;
import fullcare.backend.schedulemember.repository.ScheduleMemberRepository;
import fullcare.backend.util.CustomPageImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EvaluationService {

    private final FinalEvaluationRepository finalEvaluationRepository;
    private final MidtermEvaluationRepository midtermEvaluationRepository;
    private final ProjectRepository projectRepository;
    private final ScheduleRepository scheduleRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final ProjectService projectService;

    private static void setBadge(List<BadgeDto> badgeDtoDtos) {
        List<EvaluationBadge> evaluationBadges = new ArrayList<>();
        evaluationBadges.add(EvaluationBadge.탁월한_리더);
        evaluationBadges.add(EvaluationBadge.열정적인_참여자);
        evaluationBadges.add(EvaluationBadge.최고의_서포터);
        evaluationBadges.add(EvaluationBadge.아이디어_뱅크);
        for (BadgeDto badgeDto : badgeDtoDtos) {
            if (badgeDto.getEvaluationBadge().equals(EvaluationBadge.탁월한_리더)) {
                evaluationBadges.remove(EvaluationBadge.탁월한_리더);
            } else if (badgeDto.getEvaluationBadge().equals(EvaluationBadge.열정적인_참여자)) {
                evaluationBadges.remove(EvaluationBadge.열정적인_참여자);
            } else if (badgeDto.getEvaluationBadge().equals(EvaluationBadge.최고의_서포터)) {
                evaluationBadges.remove(EvaluationBadge.최고의_서포터);
            } else if (badgeDto.getEvaluationBadge().equals(EvaluationBadge.아이디어_뱅크)) {
                evaluationBadges.remove(EvaluationBadge.아이디어_뱅크);
            }
        }
        for (EvaluationBadge evaluationBadge : evaluationBadges) { // 없는 뱃지 추가
            badgeDtoDtos.add(new BadgeDto(evaluationBadge, 0l));
        }
    }

    public boolean validateFinalDuplicationAuthor(Long evaluatedId, Long authorId, Long projectId) {
        return finalEvaluationRepository.existsByEvaluatedIdAndEvaluatorIdAndProjectId(evaluatedId, authorId, projectId);
    }
    public boolean validateMidEvalDuplicationAuthor(Long scheduleId, Long voterId) {
        return midtermEvaluationRepository.existsByScheduleIdAndVoterId(scheduleId, voterId);
    }

    @Transactional
    public void createMidtermEvaluation(MidTermEvalCreateRequest midTermEvalCreateRequest, Long voterId) {
        projectService.isProjectAvailable(midTermEvalCreateRequest.getProjectId(), voterId, false);
        ProjectMember voter = projectMemberRepository.findByProjectIdAndMemberId(midTermEvalCreateRequest.getProjectId(), voterId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
        ProjectMember voted = projectMemberRepository.findByProjectIdAndMemberId(midTermEvalCreateRequest.getProjectId(), midTermEvalCreateRequest.getVotedId()).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
        if (voter.getId() == voted.getId()) {
            throw new SelfEvalException(EvaluationErrorCode.SELF_EVALUATION);
        }
        if (validateMidEvalDuplicationAuthor(midTermEvalCreateRequest.getScheduleId(), voter.getId())) {
            throw new DuplicateEvalException(EvaluationErrorCode.DUPLICATE_EVALUATION);
        }
        scheduleMemberRepository.findByScheduleIdAndPmId(midTermEvalCreateRequest.getScheduleId(), voter.getId()).orElseThrow(() -> new EntityNotFoundException(ScheduleErrorCode.SCHEDULE_MEMBER_NOT_FOUND));
        scheduleMemberRepository.findByScheduleIdAndPmId(midTermEvalCreateRequest.getScheduleId(), voted.getId()).orElseThrow(() -> new EntityNotFoundException(ScheduleErrorCode.SCHEDULE_MEMBER_NOT_FOUND));
        Schedule schedule = scheduleRepository.findByIdAndState(midTermEvalCreateRequest.getScheduleId(), State.COMPLETE).orElseThrow(() -> new UnauthorizedAccessException(EvaluationErrorCode.MID_EVAL_NOT_CREATE));
        Project project = projectRepository.findById(midTermEvalCreateRequest.getProjectId()).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
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
    public Long createFinalEvaluation(FinalEvalCreateRequest finalEvalCreateRequest, Long evaluatorId) {
        ProjectMember evaluator = projectMemberRepository.findByProjectIdAndMemberId(finalEvalCreateRequest.getProjectId(), evaluatorId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
        ProjectMember evaluated = projectMemberRepository.findByProjectIdAndMemberId(finalEvalCreateRequest.getProjectId(), finalEvalCreateRequest.getEvaluatedId()).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
        Project project = projectRepository.findById(finalEvalCreateRequest.getProjectId()).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
        //? 점수 5점 이상일 경우 에러처리
        if (!Score.valid(finalEvalCreateRequest.getScore())) {
            throw new EvalOutOfRangeException(EvaluationErrorCode.SCORE_OUT_OF_RANGE);
        }
        if (evaluator.getId() == evaluated.getId()) {
            throw new SelfEvalException(EvaluationErrorCode.SELF_EVALUATION);
        }
        if (!project.getState().equals(State.COMPLETE)) {
            throw new NotCompletedProjectException(ProjectErrorCode.PROJECT_UNCOMPLETED);
        }
        if ((validateFinalDuplicationAuthor(finalEvalCreateRequest.getEvaluatedId(), evaluator.getId(), finalEvalCreateRequest.getProjectId()))) {
            throw new DuplicateEvalException(EvaluationErrorCode.DUPLICATE_EVALUATION);
        }
        FinalTermEvaluation newFinalTermEvaluation = FinalTermEvaluation.createNewFinalEval()
                .project(project)
                .content(finalEvalCreateRequest.getContent())
                .score(finalEvalCreateRequest.getScore())
                .evaluator(evaluator)
                .evaluated(evaluated)
                .build();
        FinalTermEvaluation finalEval = finalEvaluationRepository.save(newFinalTermEvaluation);
        return finalEval.getId();
    }

    public List<BadgeDto> findMidtermEvaluationDetailResponse(Long projectId, Long memberId) {
        projectService.isProjectAvailable(projectId, memberId, true);
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow();
        List<BadgeDto> midtermDetailResponses = midtermEvaluationRepository.findAllByMemberId(projectId, projectMember.getId());
        setBadge(midtermDetailResponses);
        return midtermDetailResponses;
    }

    public FinalEvaluationResponse findFinalEvaluationDetailResponse(Long projectId, Long memberId, Long evaluationId) {
        projectService.isProjectAvailable(projectId, memberId, true);
        FinalTermEvaluation findFinalTermEvaluation = finalEvaluationRepository.findById(evaluationId).orElseThrow(() -> new EntityNotFoundException(EvaluationErrorCode.EVALUATION_NOT_FOUND));
        return FinalEvaluationResponse.entityToDto(findFinalTermEvaluation);
    }

    public EverythingEvalResponse findMidtermEvaluationList(Long projectId, Long memberId) {
        ProjectMember projectMember = projectService.isProjectAvailable(projectId, memberId, true);
        List<Member> members = projectMember.getProject().getProjectMembers().stream().map(pm -> pm.getMember()).collect(Collectors.toList());
        List<ProjectMember> projectMembers = projectMember.getProject().getProjectMembers();
        List<BadgeDao> midtermBadgeList = midtermEvaluationRepository.findList(projectId, projectMembers);
        List<MidTermRankProjectionInterface> rank = midtermEvaluationRepository.findRank(projectId);

        List<MidChartDto> midTermEvalChartDto = members.stream().map(m -> MidChartDto.builder()
                .memberId(m.getId())
                .name(m.getName())
                .build()
        ).collect(Collectors.toList());
        for (BadgeDao badgeDao : midtermBadgeList) {
            for (MidChartDto chart : midTermEvalChartDto) {
                if (badgeDao.getMemberId() == chart.getMemberId()) {
                    chart.getEvaluation().put(badgeDao.getEvaluationBadge(), badgeDao.getQuantity());
                }
            }
        }

        List<MidTermRankingDto> rankingDtos = new ArrayList<>();// 랭킹 부분
        for (MidTermRankProjectionInterface r : rank) {
            String name = projectMembers.stream().filter(pm -> pm.getId() == r.getId()).map(pm -> pm.getMember().getName()).findFirst().get();
            rankingDtos.add(MidTermRankingDto.builder()
                    .rank(r.getRanking())
                    .name(name)
                    .quantity(r.getQuantity())
                    .build()
            );
        }

        for (MidChartDto chartDto : midTermEvalChartDto) {
            chartDto.getEvaluation().putIfAbsent(EvaluationBadge.아이디어_뱅크, 0l);
            chartDto.getEvaluation().putIfAbsent(EvaluationBadge.최고의_서포터, 0l);
            chartDto.getEvaluation().putIfAbsent(EvaluationBadge.탁월한_리더, 0l);
            chartDto.getEvaluation().putIfAbsent(EvaluationBadge.열정적인_참여자, 0l);
        }
        EverythingEvalResponse everythingEvalResponse = new EverythingEvalResponse(midTermEvalChartDto, rankingDtos, rankingDtos.size() != 0 ? true : false);
        return everythingEvalResponse;
    }

    public EverythingEvalResponse findFinalEvaluationList(Long projectId, Long memberId) {
        ProjectMember projectMember = projectService.isProjectAvailable(projectId, memberId, true);
        Project project = projectMember.getProject();
        List<ProjectMember> projectMembers = project.getProjectMembers();
        List<ScoreDao> scoreDaos = finalEvaluationRepository.findList(project.getId(), projectMembers);

        List<FinalCharDto> finalTermEvalFinalCharDto = projectMembers.stream().map(pm -> FinalCharDto.builder()
                .id(pm.getMember().getId())
                .name(pm.getMember().getName())
                .build()
        ).collect(Collectors.toList());
        for (ScoreDao s : scoreDaos) {
            for (FinalCharDto chart : finalTermEvalFinalCharDto) {
                if (s.getId() == chart.getId()) {
                    chart.addEvaluation(ScoreDto.builder()
                            .jobPerformance(s.getJobPerformance())
                            .punctuality(s.getPunctuality())
                            .communication(s.getCommunication())
                            .sincerity(s.getSincerity()).build());
                }
            }
        }
        finalTermEvalFinalCharDto.stream().filter(fe -> fe.getEvaluation().size() == 0).forEach(fe -> fe.addEvaluation(new ScoreDto()));

        // * 랭킹 부분
        List<FinalTermRankingDto> rankingDtos = finalTermEvalFinalCharDto.stream().map(fe -> FinalTermRankingDto.builder()
                .memberId(fe.getId())
                .name(fe.getName())
                .score(Score.avg((ScoreDto) fe.getEvaluation().get(0)))
                .build()).collect(Collectors.toList());
        Collections.sort(rankingDtos, (a, b) -> (int) Math.round(b.getScore() - a.getScore()));
        List<FinalTermRankingDto> ranks = rankingDtos.stream().filter(r -> r.getScore() != 0).collect(Collectors.toList());// 0점 랭킹에서 제거

        for (FinalTermRankingDto exRank : rankingDtos) {
            Long rank = 1l;
            for (FinalTermRankingDto inRank : rankingDtos) {
                if (exRank.getScore() < inRank.getScore()) {
                    rank++;
                }
            }
            exRank.setRank(rank);
        }

        EverythingEvalResponse everythingEvalResponse = new EverythingEvalResponse(finalTermEvalFinalCharDto, ranks, ranks.size() != 0 ? true : false);
        return everythingEvalResponse;
    }

    public MidTermEvalModalResponse modal(Long scheduleId, Long memberId) {
        Schedule schedule = scheduleRepository.findJoinSMJoinMemberById(scheduleId).orElseThrow(() -> new EntityNotFoundException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        Long projectId = schedule.getProject().getId();
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
        List<EvalMemberDto> evalMemberDto = schedule.getScheduleMembers().stream().filter(sm -> sm.getProjectMember() != projectMember).map(sm -> EvalMemberDto.builder().projectMember(sm.getProjectMember()).build()).collect(Collectors.toList());// 나를 제외한 일정에 참여한 팀원


        MidTermEvalModalResponse midTermEvalModalResponse = MidTermEvalModalResponse.builder()
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .state(schedule.getState())
                .build();
        midTermEvalModalResponse.setMembers(evalMemberDto);
        List<BadgeDto> badgeDtoDtos = new ArrayList<>();
        setBadge(badgeDtoDtos);
        midTermEvalModalResponse.setBadgeDtos(badgeDtoDtos);
        return midTermEvalModalResponse;
    }

    public List<ParticipantResponse> findParticipantList(Long projectId, Long memberId) {
        ProjectMember projectMember = projectService.isProjectAvailable(projectId, memberId, true);
        Project project = projectMember.getProject();
        List<ProjectMember> projectMembers = project.getProjectMembers();
        List<BadgeDao> midtermBadgeList = midtermEvaluationRepository.findList(project.getId(), projectMembers);
        List<FinalTermEvaluation> finalEvalList = new ArrayList<>();
        if (project.getState().equals(State.COMPLETE)) {
            finalEvalList = finalEvaluationRepository.findByProjectIdAndEvaluatorId(project.getId(), projectMember.getId());
        }
        List<ParticipantResponse> response = new ArrayList<>();
        for (ProjectMember pm : projectMembers) {
            Member member = pm.getMember();
            ParticipantResponse participantResponse = ParticipantResponse.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .imageUrl(member.getImageUrl())
                    .isMe(member.getId() == memberId)
                    .build();
            for (BadgeDao badgeDao : midtermBadgeList) {
                if (member.getId() == badgeDao.getMemberId()) {
                    BadgeDto badgeDto = new BadgeDto(badgeDao.getEvaluationBadge(), badgeDao.getQuantity());
                    participantResponse.addBadge(badgeDto);
                }
            }
            for (FinalTermEvaluation fe : finalEvalList) { // * 로그인한 사용자가 다른사람 최종평가를 작성한적이 있으면 최종평가 ID 추가 없으면 null
                if (project.getState().equals(State.COMPLETE) && fe.getEvaluated() == pm) {
                    participantResponse.setFinalEvalId(fe.getId());
                }
            }
            setBadge(participantResponse.getBadgeDtos());// * 개수가 0인 뱃지 설정
            response.add(participantResponse);
        }

        return response;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // * 개인 페이지 서비스
    public Page<MyEvalListResponse> findMyEvalList(Pageable pageable, Long memberId) {
        Page<ProjectMember> pmList = projectMemberRepository.findByMemberId(pageable, memberId);
        List<MyEvalListResponse> myEvalListResponseList = new ArrayList<>();
        List<Long> projectIds = pmList.stream().map(pm -> pm.getProject().getId()).collect(Collectors.toList());

        List<FinalTermEvaluation> myFinalEvalList = finalEvaluationRepository.findByProjectIdsAndEvaluatedIdAndState(projectIds, memberId);
        int feCnt = 0;
        for (ProjectMember pm : pmList) {
            feCnt = 0;
            ScoreDto scoreDto = new ScoreDto();
            for (FinalTermEvaluation fe : myFinalEvalList) {
                if (fe.getProject().getId() == pm.getProject().getId()) {
                    feCnt++;
                    scoreDto.setCommunication(scoreDto.getCommunication() + fe.getScore().getCommunication());
                    scoreDto.setPunctuality(scoreDto.getPunctuality() + fe.getScore().getPunctuality());
                    scoreDto.setSincerity(scoreDto.getSincerity() + fe.getScore().getSincerity());
                    scoreDto.setJobPerformance(scoreDto.getJobPerformance() + fe.getScore().getJobPerformance());
                }
            }
            scoreDto.setSincerity(Math.round(scoreDto.getSincerity() / feCnt * 10) / 10.0);
            scoreDto.setJobPerformance(Math.round(scoreDto.getJobPerformance() / feCnt * 10) / 10.0);
            scoreDto.setPunctuality(Math.round(scoreDto.getPunctuality() / feCnt * 10) / 10.0);
            scoreDto.setCommunication(Math.round(scoreDto.getCommunication() / feCnt * 10) / 10.0);

            MyEvalListResponse response = MyEvalListResponse.builder()
                    .projectId(pm.getProject().getId())
                    .projectTitle(pm.getProject().getTitle())
                    .score(scoreDto)
                    .build();

            myEvalListResponseList.add(response);
        }


        return new CustomPageImpl<>(myEvalListResponseList, pageable, pmList.getTotalElements());
    }

    public MyEvalDetailResponse findMyEval(Long projectId, Long memberId) {
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
        List<BadgeDto> badgeList = midtermEvaluationRepository.findAllByMemberId(projectId, projectMember.getId());
        BadgeSubDto badgeSubDto = new BadgeSubDto();
        for (BadgeDto badgeDto : badgeList) {
            if (badgeDto.getEvaluationBadge() == EvaluationBadge.최고의_서포터){
                badgeSubDto.updateSupport(badgeSubDto.getSupport(), badgeDto.getQuantity());
            }else if(badgeDto.getEvaluationBadge() == EvaluationBadge.탁월한_리더){
                badgeSubDto.updateLeader(badgeSubDto.getSupport(), badgeDto.getQuantity());
            }else if(badgeDto.getEvaluationBadge() == EvaluationBadge.열정적인_참여자){
                badgeSubDto.updateParticipant(badgeSubDto.getSupport(), badgeDto.getQuantity());
            }else if(badgeDto.getEvaluationBadge() == EvaluationBadge.아이디어_뱅크){
                badgeSubDto.updateBank(badgeSubDto.getSupport(), badgeDto.getQuantity());
            }
        }
        List<FinalTermEvaluation> myFinalEvalList = finalEvaluationRepository.findByProjectIdAndEvaluatedId(projectId, projectMember.getId()); // * 최종평가가 완료된 것만 조회, 임시저장 X
        List<FinalEvalDto> finalEvalDtoList = new ArrayList<>();
        for (FinalTermEvaluation fe : myFinalEvalList) {
            ScoreDto scoreDto = new ScoreDto();
            Member member = fe.getEvaluator().getMember();
            scoreDto.setCommunication(fe.getScore().getCommunication());
            scoreDto.setPunctuality(fe.getScore().getPunctuality());
            scoreDto.setSincerity(fe.getScore().getSincerity());
            scoreDto.setJobPerformance(fe.getScore().getJobPerformance());
            FinalEvalDto finalEvalDto = FinalEvalDto.builder()
                    .memberId(fe.getEvaluator().getId())
                    .memberName(member.getName())
                    .imageUrl(member.getImageUrl())
                    .content(fe.getContent())
                    .score(scoreDto)
                    .build();
            finalEvalDtoList.add(finalEvalDto);

        }
        return new MyEvalDetailResponse(badgeSubDto, finalEvalDtoList);
    }

    public MyEvalChartResponse findMyEvalChart(Long memberId) {
        List<ScoreDao> myAvgScoreList = finalEvaluationRepository.findMyAvgScore(memberId);
        double sincerity = 0;
        double jobPerformance = 0;
        double punctuality = 0;
        double communication = 0;
        for (ScoreDao scoreDao : myAvgScoreList) {
            sincerity += scoreDao.getSincerity();
            jobPerformance += scoreDao.getJobPerformance();
            punctuality += scoreDao.getPunctuality();
            communication += scoreDao.getCommunication();
        }
        ScoreDto score = new ScoreDto();
        score.setSincerity(Math.round((sincerity/myAvgScoreList.size())*10)/10.0);
        score.setJobPerformance(Math.round(jobPerformance/myAvgScoreList.size()*10)/10.0);
        score.setPunctuality(Math.round(punctuality/myAvgScoreList.size()*10)/10.0);
        score.setCommunication(Math.round(communication/myAvgScoreList.size()*10)/10.0);
        return new MyEvalChartResponse(score);
    }


}
