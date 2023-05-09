package fullcare.backend.projectmember.domain;


import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class ProjectMemberRole {

    @Enumerated(EnumType.STRING)
    private ProjectMemberRoleType role; // * 리더 or 팀원

    @Enumerated(EnumType.STRING)
    private ProjectMemberRoleType position; // * 직무 포지션

    public ProjectMemberRole(ProjectMemberRoleType role, ProjectMemberRoleType position) {
        this.role = role;
        this.position = position;
    }
}
