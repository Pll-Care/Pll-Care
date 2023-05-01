package fullcare.backend.project;


import fullcare.backend.evaluation.domain.Evaluation;
import fullcare.backend.memo.Memo;
import fullcare.backend.post.domain.Post;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
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

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private ArrayList<Evaluation> evaluations = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private ArrayList<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private ArrayList<Memo> memos = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private ArrayList<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private ArrayList<ProjectMember> projectMembers = new ArrayList<>();


}
