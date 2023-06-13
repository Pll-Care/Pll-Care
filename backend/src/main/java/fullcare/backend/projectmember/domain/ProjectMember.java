package fullcare.backend.projectmember.domain;

import fullcare.backend.member.domain.Member;
import fullcare.backend.project.domain.Project;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "project_member")
@Table(name = "project_member")
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Embedded
    private ProjectMemberRole projectMemberRole; // 리더 or 팀원


    @Builder(builderMethodName = "createNewProjectMember")
    public ProjectMember(Member member, Project project, ProjectMemberRole projectMemberRole) {
        this.member = member;
        this.project = project;
        this.projectMemberRole = projectMemberRole;

    }
    
    public boolean isLeader() {
        return (this.projectMemberRole.getRole() == ProjectMemberRoleType.리더) ? true : false;
    }


}
