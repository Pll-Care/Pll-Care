package fullcare.backend.project.domain;


import fullcare.backend.evaluation.domain.Evaluation;
import fullcare.backend.member.domain.Member;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.post.domain.Post;
import fullcare.backend.project.dto.ProjectCreateRequest;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.domain.ProjectMemberRole;
import fullcare.backend.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "project")
@Table(name="project")
public class Project {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "create_dt", nullable = false)
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Evaluation> evaluations = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Memo> memos = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProjectMember> projectMembers = new ArrayList<>();


    @Column(name = "start_dt", nullable = false)
    private LocalDateTime startDate;
    @Column(name = "end_dt", nullable = false)
    private LocalDateTime endDate;


    public Project(ProjectCreateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
    }
    public void updateState(State state){
        this.state = state;
    }
    public void addMember(Member member,  ProjectMemberRole role){
        ProjectMember pm = ProjectMember.builder()
                .member(member)
                .project(this)
                .role(role).build();
        projectMembers.add(pm);
    }

    public void setCreateDate(LocalDateTime createDate){
        this.createDate =createDate;
    }
}
