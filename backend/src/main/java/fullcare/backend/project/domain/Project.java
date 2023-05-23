package fullcare.backend.project.domain;


import fullcare.backend.evaluation.domain.FinalTermEvaluation;
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

//    @Column(name = "create_dt", nullable = false)
//    private LocalDateTime createDate;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinalTermEvaluation> evaluations = new ArrayList<>();

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
    public Project(State state, String title, String description, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.description = description;
        this.state = state;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void updateState(State state) {
        this.state = state;
    }

    public void addMember(Member member, ProjectMemberRole role) {
        ProjectMember pm = ProjectMember.createNewProjectMember()
                .member(member)
                .project(this)
                .role(role).build();

        projectMembers.add(pm); // ! project save시에 projectmember도 cascade 옵션으로 똑바로 저장되는지 확인 필요
        member.getProjectMembers().add(pm); // ? member 엔티티에서 projectMembers 컬렉션은 필요없을듯?
    }

    public void update(ProjectUpdateRequest projectUpdateRequest) {
        this.title = projectUpdateRequest.getTitle();
        this.description = projectUpdateRequest.getDescription();
        this.startDate = projectUpdateRequest.getStartDate();
        this.endDate = projectUpdateRequest.getEndDate();
        this.state = projectUpdateRequest.getState();
    }
}
