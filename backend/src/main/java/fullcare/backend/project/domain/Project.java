package fullcare.backend.project.domain;


import fullcare.backend.evaluation.domain.FinalTermEvaluation;
import fullcare.backend.evaluation.domain.MidtermEvaluation;
import fullcare.backend.global.State;
import fullcare.backend.global.entity.BaseEntity;
import fullcare.backend.member.domain.Member;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.post.domain.Post;
import fullcare.backend.project.dto.request.ProjectUpdateRequest;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.domain.ProjectMemberRole;
import fullcare.backend.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.*;

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

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    private String imageUrl;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MidtermEvaluation> midtermEvaluations = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinalTermEvaluation> finalTermEvaluations = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Memo> memos = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> projectMembers = new ArrayList<>();


    @Column(name = "start_dt", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_dt", nullable = false)
    private LocalDate endDate;


    @Builder(builderMethodName = "createNewProject")
    public Project(State state, String title, String description, LocalDate startDate, LocalDate endDate, String imageUrl) {
        this.title = title;
        this.description = description;
        this.state = state;
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

    public void addMember(Member member, ProjectMemberRole role) {
        ProjectMember pm = ProjectMember.createNewProjectMember()
                .member(member)
                .project(this)
                .projectMemberRole(role).build();

        projectMembers.add(pm);
    }

    public void update(ProjectUpdateRequest projectUpdateRequest) {
        this.title = projectUpdateRequest.getTitle();
        this.description = projectUpdateRequest.getDescription();
        this.startDate = projectUpdateRequest.getStartDate();
        this.endDate = projectUpdateRequest.getEndDate();
        this.state = projectUpdateRequest.getState();
        this.imageUrl = projectUpdateRequest.getImageUrl();
    }
    public boolean isCompleted(){
        return this.state.equals(State.COMPLETE) ? true : false;
    }
}
