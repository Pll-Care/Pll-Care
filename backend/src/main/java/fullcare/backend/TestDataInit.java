package fullcare.backend;


import fullcare.backend.evaluation.domain.EvaluationBadge;
import fullcare.backend.evaluation.domain.FinalTermEvaluation;
import fullcare.backend.evaluation.domain.MidtermEvaluation;
import fullcare.backend.evaluation.domain.Score;
import fullcare.backend.evaluation.repository.FinalEvaluationRepository;
import fullcare.backend.evaluation.repository.MidtermEvaluationRepository;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.domain.MemberRole;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.memo.dto.request.MemoBookmarkRequest;
import fullcare.backend.memo.dto.request.MemoCreateRequest;
import fullcare.backend.memo.service.MemoService;
import fullcare.backend.post.dto.request.PostCreateRequest;
import fullcare.backend.post.service.PostService;
import fullcare.backend.profile.domain.Contact;
import fullcare.backend.profile.domain.Profile;
import fullcare.backend.profile.dto.ProjectExperienceRequestDto;
import fullcare.backend.profile.dto.request.ProfileUpdateRequest;
import fullcare.backend.profile.service.ProfileService;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.request.ProjectApplyRequest;
import fullcare.backend.project.dto.request.ProjectCreateRequest;
import fullcare.backend.project.service.ProjectService;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.domain.ProjectMemberPositionType;
import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import fullcare.backend.projectmember.domain.ProjectMemberType;
import fullcare.backend.projectmember.service.ProjectMemberService;
import fullcare.backend.recruitment.domain.RecruitInfo;
import fullcare.backend.recruitment.domain.RecruitPosition;
import fullcare.backend.schedule.ScheduleCategory;
import fullcare.backend.schedule.domain.Schedule;
import fullcare.backend.schedule.dto.request.ScheduleCreateRequest;
import fullcare.backend.schedule.repository.ScheduleRepository;
import fullcare.backend.schedule.service.MeetingService;
import fullcare.backend.schedule.service.MilestoneService;
import fullcare.backend.schedule.service.ScheduleService;
import fullcare.backend.util.dto.TechStack;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class TestDataInit {
    private final MemberRepository memberRepository;
    private final ProjectService projectService;
    private final MeetingService meetingService;
    private final MilestoneService milestoneService;
    private final MemoService memoService;

    private final PostService postService;
    private final MidtermEvaluationRepository midtermEvaluationRepository;
    private final FinalEvaluationRepository finalEvaluationRepository;
    private final ScheduleService scheduleService;
    private final ScheduleRepository scheduleRepository;
    private final ProfileService profileService;
    private final ProjectMemberService projectMemberService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initData() {
        createMember();
        createProject();
        createSchedule();
        createMidEvaluation();
        createFinalEvaluation();
        createProfile();
        createMemoAndBookMark();
        createPostAndLikes();
        createApply();

    }


    private void createMemoAndBookMark() {
        Random rand = new Random();
        for (long i = 1l; i < 11l; i++) {
            for (long j = 1l; j < 10l; j++) {

                Long memoId = memoService.createMemo(i, new MemoCreateRequest(j, "제목" + i, "내용" + i));

                if (i % 4 == 0 || j % 3 == 0) {
                    long randomMemberId = rand.nextLong(1, 11);
                    long randomProjectId = rand.nextLong(1, 10);

                    memoService.bookmarkMemo(randomMemberId, memoId, new MemoBookmarkRequest(randomProjectId));
                }

            }


        }
    }

    private void createPostAndLikes() {
        Random rand = new Random();
        for (long i = 1l; i < 25; i++) {
//            LocalDate startDate = LocalDate.of(2023, 1, 1);
//            LocalDate endDate = LocalDate.of(2023, 12, 31);


            RecruitInfo recruitInfo1 = new RecruitInfo(ProjectMemberPositionType.백엔드, 0, rand.nextInt(1, 5));
            RecruitInfo recruitInfo2 = new RecruitInfo(ProjectMemberPositionType.프론트엔드, 0, rand.nextInt(1, 5));
            RecruitInfo recruitInfo3 = new RecruitInfo(ProjectMemberPositionType.디자인, 0, rand.nextInt(1, 5));
            RecruitInfo recruitInfo4 = new RecruitInfo(ProjectMemberPositionType.기획, 0, rand.nextInt(1, 5));

            ArrayList<RecruitInfo> recruitInfos = new ArrayList<>();
            recruitInfos.add(recruitInfo1);
            recruitInfos.add(recruitInfo2);
            recruitInfos.add(recruitInfo3);
            recruitInfos.add(recruitInfo4);

            List<TechStack> techStacks = new ArrayList<>();
            techStacks.add(TechStack.Svelte);
            techStacks.add(TechStack.React);
            techStacks.add(TechStack.Firebase);
            techStacks.add(TechStack.Django);
            techStacks.add(TechStack.Git);
            techStacks.add(TechStack.Spring);
            techStacks.add(TechStack.SpringBoot);

            long randomMemberId = rand.nextLong(1, 11);
            long randomProjectId = rand.nextLong(1, 10);


            if (i < 10) {
                Project project1 = projectService.findProject(i);
                Long postId = postService.createPost(randomMemberId, new PostCreateRequest(i, "모집글" + i, "내용" + i, project1.getStartDate().plusMonths(1l), project1.getEndDate().minusWeeks(1l), "참조" + i, "연락" + i, "지역" + i, techStacks, recruitInfos));

                postService.likePost(rand.nextLong(1, 11), postId);
            }


            Project project2 = projectService.findProject(randomProjectId);
            Long postId = postService.createPost(randomMemberId, new PostCreateRequest(randomProjectId, "모집글" + i, "내용" + i, project2.getStartDate().plusMonths(1l), project2.getEndDate().minusWeeks(1l), "참조" + i, "연락" + i, "지역" + i, techStacks, recruitInfos));

            if (i % 3 == 0) {
                postService.likePost(randomMemberId, postId);
            }
        }
    }

    private void createApply() {
        for (long i = 11l; i < 21; i++) {
            Random rand = new Random();
            long randomPostId = rand.nextLong(1, 34);

            postService.applyProjectByPost(i, randomPostId, new ProjectApplyRequest(ProjectMemberPositionType.백엔드));
//            postService.applyProjectByPost(i, randomPostId, new ProjectApplyRequest(ProjectMemberPositionType.프론트엔드));
//            postService.applyProjectByPost(i, randomPostId, new ProjectApplyRequest(ProjectMemberPositionType.디자인));
//            postService.applyProjectByPost(i, randomPostId, new ProjectApplyRequest(ProjectMemberPositionType.기획));
        }
    }

    private void createProfile() {
        Member member = memberRepository.findById(1l).get();
        Contact contact = new Contact("email@naver.com", "git@naver.com", "url.com");
        List<TechStack> techStacks = new ArrayList<>();
        techStacks.add(TechStack.Svelte);
        techStacks.add(TechStack.React);
        List<ProjectExperienceRequestDto> projectExperiences = new ArrayList<>();
        projectExperiences.add(ProjectExperienceRequestDto.builder().title("title").description("설명").startDate(LocalDate.now()).endDate(LocalDate.now().plusWeeks(1l)).techStack(techStacks).build());
        List<TechStack> techStacks2 = new ArrayList<>();
        techStacks2.add(TechStack.Firebase);
        techStacks2.add(TechStack.Django);
        projectExperiences.add(ProjectExperienceRequestDto.builder().title("title1").description("설명2222").startDate(LocalDate.now()).endDate(LocalDate.now().plusWeeks(5l)).techStack(techStacks2).build());
        profileService.updateProfile(member, new ProfileUpdateRequest(contact, RecruitPosition.백엔드, techStacks, null, projectExperiences, false));


    }

    private void createFinalEvaluation() {
        Random rand = new Random();
        Project project = projectService.findProject(1l);
        List<ProjectMember> projectMembers = project.getProjectMembers();
        for (long i = 1l; i <= 30; i++) {
            int evaluatorId = rand.nextInt(1, 10);
            int evaluatedId = rand.nextInt(1, 10);
            ProjectMember evaluator = projectMembers.get(evaluatorId);
            ProjectMember evaluated = projectMembers.get(0);
            FinalTermEvaluation finalTermEvaluation = FinalTermEvaluation.createNewFinalEval()
                    .content("내용")
                    .score(new Score(rand.nextInt(5), rand.nextInt(5), rand.nextInt(5), rand.nextInt(5)))
                    .evaluator(evaluator)
                    .evaluated(evaluated)
                    .project(project)
//                    .state(State.COMPLETE)
                    .build();
            finalEvaluationRepository.save(finalTermEvaluation);
        }
    }

    private void createMidEvaluation() {
        Random rand = new Random();
        Project project = projectService.findProject(1l);
        List<ProjectMember> projectMembers = project.getProjectMembers();
        for (long i = 1l; i <= 30; i++) {
            int voterId = rand.nextInt(1, 10);
            int votedId = rand.nextInt(1, 10);
            ProjectMember voter = projectMembers.get(voterId);
            ProjectMember voted = projectMembers.get(0);
            EvaluationBadge badge = null;
            if (i % 4 == 0) {
                badge = EvaluationBadge.아이디어_뱅크;
            } else if (i % 4 == 1) {
                badge = EvaluationBadge.최고의_서포터;
            } else if (i % 4 == 2) {
                badge = EvaluationBadge.탁월한_리더;
            } else {
                badge = EvaluationBadge.열정적인_참여자;
            }
            Schedule schedule = scheduleRepository.findById(1l).orElseThrow();
            MidtermEvaluation midtermEvaluation = MidtermEvaluation.createNewMidtermEval()
                    .voter(voter)
                    .voted(voted)
                    .project(project)
                    .schedule(schedule)
                    .evaluationBadge(badge).build();
            midtermEvaluationRepository.save(midtermEvaluation);
        }
    }


    private void createSchedule() {
        Random rand = new Random();
        Set<Long> insertId = new HashSet<>();
        long authorId;
        for (long i = 1l; i <= 30; i++) {
            authorId = rand.nextLong(1, 10);
            List<Long> memberIds = new ArrayList<>();
            long lValue = rand.nextLong(3, 6);
            for (long j = 0l; j < lValue; j++) {
                long findMemberId = rand.nextLong(1, 10);

                if (!insertId.contains(findMemberId)) {
                    Member member = memberRepository.findById(findMemberId).get();
                    memberIds.add(member.getId());
                } else {
                    j--;
                }
                insertId.add(findMemberId);
            }
            insertId.clear();
            Month month = LocalDateTime.now().getMonth();
            int randMonth = rand.nextInt(1, 12);

            LocalDate localDate = LocalDateTime.now().toLocalDate();
            int lastDay = LocalDateTime.now().toLocalDate().withDayOfMonth(localDate.lengthOfMonth()).getDayOfMonth();
            int plusDay = rand.nextInt(3, 7);
            int randDay = rand.nextInt(1, lastDay);
//            LocalDateTime startDate = LocalDateTime.of(2023, randMonth, randDay, 13, 0);
            LocalDateTime startDate = LocalDateTime.of(2023, month, randDay, 13, 0);

            if (i < 16) {
                LocalDateTime endDate = LocalDateTime.of(2023, month, randDay, 16, 0);
                ProjectMember projectMember = projectService.isProjectAvailable(1l, 1l, false);
                meetingService.createMeeting(new ScheduleCreateRequest(1l, startDate, endDate, ScheduleCategory.MEETING, memberIds, "제목" + i, "내용" + i, "address" + i), projectMember.getMember().getId());
            } else {
                try {
                    LocalDateTime endDate = LocalDateTime.of(2023, month, randDay + plusDay, 16, 0);
                    ProjectMember projectMember = projectService.isProjectAvailable(1l, 1l, false);
                    milestoneService.createMilestone(new ScheduleCreateRequest(1l, startDate, endDate, ScheduleCategory.MILESTONE, memberIds, "제목" + i, "내용" + i, null), projectMember.getMember().getId());
                } catch (DateTimeException e) { // 다음달로 넘어가는 경우
                    LocalDateTime endDate = LocalDateTime.of(2023, month, randDay, 16, 0).plusWeeks(1);
                    ProjectMember projectMember = projectService.isProjectAvailable(1l, 1l, false);
                    milestoneService.createMilestone(new ScheduleCreateRequest(1l, startDate, endDate, ScheduleCategory.MILESTONE, memberIds, "제목" + i, "내용" + i, null), projectMember.getMember().getId());
                }
            }
        }
    }

    private void createProject() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        for (long i = 1l; i < 10l; i++) {
            Project project = projectService.createProject(1l, new ProjectCreateRequest("제목" + i, "내용" + i, startDate.plusMonths(i), endDate, null));
            for (long j = 2l; j < 11l; j++) {
                if (j < 4l) {
                    project.addMember(memberRepository.findById(j).get(), new ProjectMemberType(ProjectMemberRoleType.팀원, ProjectMemberPositionType.백엔드)); // 2~3
                } else if (j < 7l) {
                    project.addMember(memberRepository.findById(j).get(), new ProjectMemberType(ProjectMemberRoleType.팀원, ProjectMemberPositionType.프론트엔드)); // 4~6
                } else if (j < 9l) {
                    project.addMember(memberRepository.findById(j).get(), new ProjectMemberType(ProjectMemberRoleType.팀원, ProjectMemberPositionType.디자인)); // 7~8
                } else {
                    project.addMember(memberRepository.findById(j).get(), new ProjectMemberType(ProjectMemberRoleType.팀원, ProjectMemberPositionType.기획)); // 9~10
                }
                // 11 ~ 20 까지는 어디에도 소속되지 않은 사용자
            }
        }
    }

    private void createMember() {
        Member member = new Member("google_101581376839285371456", "nick", "name", "fullcaredummy@gmail.com", MemberRole.USER, LocalDateTime.now(), "refresh", new Profile("한 줄 소개 입니다."));
        memberRepository.save(member);
        for (int i = 1; i < 20; i++) {
            member = new Member("id" + i, "nick" + i, "name" + i, "email" + i, MemberRole.USER, LocalDateTime.now(), "refresh" + i, new Profile("한 줄 소개 입니다."));
            memberRepository.save(member);
        }
    }

}
