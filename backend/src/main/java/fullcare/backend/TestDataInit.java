package fullcare.backend;


import fullcare.backend.member.domain.Member;
import fullcare.backend.member.domain.MemberRole;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.memo.dto.request.MemoCreateRequest;
import fullcare.backend.memo.repository.MemoRepository;
import fullcare.backend.memo.service.BookmarkMemoService;
import fullcare.backend.memo.service.MemoService;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.request.ProjectCreateRequest;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.project.service.ProjectService;
import fullcare.backend.projectmember.domain.ProjectMemberRole;
import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
import fullcare.backend.schedule.ScheduleCategory;
import fullcare.backend.schedule.domain.Address;
import fullcare.backend.schedule.domain.Milestone;
import fullcare.backend.schedule.dto.MemberDto;
import fullcare.backend.schedule.dto.request.ScheduleCreateRequest;
import fullcare.backend.schedule.service.MeetingService;
import fullcare.backend.schedule.service.MilestoneService;
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
    private final BookmarkMemoService bookmarkMemoService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initData() {
        createMember();
        createProject();
        createSchedule();
        createMemoAndBookMark();

    }

    private void createMemoAndBookMark() {
        Random rand = new Random();
        for (long i=1l; i<25;i++){
            long findMemberId = rand.nextLong(1,10);
            Memo memo = memoService.createMemo(new MemoCreateRequest(1l, "제목" + i, "내용" + i), memberRepository.findById(findMemberId).get().getName());
            if(i%3==0){
            bookmarkMemoService.bookmarkMemo(memberRepository.findById(1l).get(),memo);}
        }
    }

    private void createSchedule() {
        Random rand = new Random();
        Set<Long> insertId = new HashSet<>();
        long authorId;
        for (long i = 1l; i<=30;i++){
            authorId = rand.nextLong(1,10);
            List<MemberDto> memberDtos = new ArrayList<>();
            long lValue = rand.nextLong(3,6);
            for (long j=0l;j<lValue;j++){
                long findMemberId = rand.nextLong(1,10);

                if(!insertId.contains(findMemberId)){
                Member member = memberRepository.findById(findMemberId).get();
                memberDtos.add(new MemberDto(member.getId(), member.getName()));}
                else{j--;}
                insertId.add(findMemberId);
            }
            insertId.clear();
            Month month = LocalDateTime.now().getMonth();

            LocalDate localDate = LocalDateTime.now().toLocalDate();
            int lastDay = LocalDateTime.now().toLocalDate().withDayOfMonth(localDate.lengthOfMonth()).getDayOfMonth();
            int plusDay = rand.nextInt(3, 7);
            int randDay = rand.nextInt(1, lastDay);
            LocalDateTime startDate = LocalDateTime.of(2023, month, randDay, 13, 0);
            if (i<16) {
                LocalDateTime endDate = LocalDateTime.of(2023, month, randDay, 16, 0);
                meetingService.createMeeting(new ScheduleCreateRequest(1l, startDate, endDate, ScheduleCategory.미팅, memberDtos, "제목" + i, "내용" + i, new Address("city" + i, "street" + i)), memberRepository.findById(authorId).get().getName());
            }else{
                try {
                    LocalDateTime endDate = LocalDateTime.of(2023, month, randDay + plusDay, 16, 0);
                    milestoneService.createMilestone(new ScheduleCreateRequest(1l, startDate, endDate, ScheduleCategory.개발일정, memberDtos, "제목" + i, "내용" + i, null), memberRepository.findById(authorId).get().getName());
                }catch (DateTimeException e){ // 다음달로 넘어가는 경우
                    LocalDateTime endDate = LocalDateTime.of(2023, month, randDay, 16, 0).plusWeeks(1);
                    milestoneService.createMilestone(new ScheduleCreateRequest(1l, startDate, endDate, ScheduleCategory.개발일정, memberDtos, "제목" + i, "내용" + i, null), memberRepository.findById(authorId).get().getName());
                }
            }
        }
    }

    private void createProject() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        for(long i =1l ;i<10l;i++){
            Project project = projectService.createProject(1l, new ProjectCreateRequest("제목" + i, "내용" + i, startDate.plusMonths(i), endDate));
            for (long j =2l; j<10l; j++){
                if(j<4l){project.addMember(memberRepository.findById(j).get(), new ProjectMemberRole(ProjectMemberRoleType.팀원, ProjectMemberRoleType.백엔드));}
                else if(j<7l){project.addMember(memberRepository.findById(j).get(), new ProjectMemberRole(ProjectMemberRoleType.팀원, ProjectMemberRoleType.프론트엔드));}
                else if(j<9l){project.addMember(memberRepository.findById(j).get(), new ProjectMemberRole(ProjectMemberRoleType.팀원, ProjectMemberRoleType.디자인));}
                else{project.addMember(memberRepository.findById(j).get(), new ProjectMemberRole(ProjectMemberRoleType.팀원, ProjectMemberRoleType.기획));}
            }
        }
    }

    private void createMember() {
        Member member = new Member("google_101581376839285371456", "nick","name","fullcaredummy@gmail.com", MemberRole.USER, LocalDateTime.now(),"refresh");
        memberRepository.save(member);
        for(int i =1 ;i<10;i++){
            member = new Member("id"+i, "nick"+i,"name"+i,"email"+i, MemberRole.USER, LocalDateTime.now(),"refresh"+i);
            memberRepository.save(member);
        }
    }

}
