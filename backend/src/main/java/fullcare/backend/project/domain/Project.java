package fullcare.backend.project.domain;


import fullcare.backend.evaluation.domain.FinalTermEvaluation;
import fullcare.backend.evaluation.domain.MidtermEvaluation;
import fullcare.backend.global.State;
import fullcare.backend.global.entity.BaseEntity;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.InvalidDateRangeException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.post.domain.Post;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.domain.ProjectMemberType;
import fullcare.backend.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "project")
@Table(name = "project")
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Memo> memos = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MidtermEvaluation> midtermEvaluations = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinalTermEvaluation> finalTermEvaluations = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @BatchSize(size = 16)
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> projectMembers = new ArrayList<>();


    @Column(name = "start_dt", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_dt", nullable = false)
    private LocalDate endDate;


    @Builder(builderMethodName = "createNewProject")
    public Project(String title, String description, State state, LocalDate startDate, LocalDate endDate, String imageUrl) {
        this.title = title;
        this.description = description;
        this.state = state;

        if (!startDate.isBefore(endDate)) {
            throw new InvalidDateRangeException(ProjectErrorCode.INVALID_DATE_RANGE);
        }

        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
    }

    public void complete() {
        this.state = State.COMPLETE;
    }

    public void updateState(State state) {
        this.state = state;
    }

    public void addMember(Member member, ProjectMemberType projectMemberType) {
        ProjectMember pm = ProjectMember.createNewProjectMember()
                .member(member)
                .project(this)
                .projectMemberType(projectMemberType)
                .build();

        projectMembers.add(pm);
    }

    public void updateAll(String title, String description, State state, LocalDate startDate, LocalDate endDate, String imageUrl) {
        this.title = title;
        this.description = description;
        this.state = state;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
    }


    public boolean isCompleted() {
        return this.state.equals(State.COMPLETE) ? true : false;
    }
}
