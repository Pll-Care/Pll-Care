package fullcare.backend.projectmember.domain;


import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class ProjectMemberType { // ! ProjectMemberType

    @Enumerated(EnumType.STRING)
    private ProjectMemberRoleType role; // * 리더 or 팀원

    @Enumerated(EnumType.STRING)
    private ProjectMemberPositionType position; // * 직무 포지션

    public ProjectMemberType(ProjectMemberRoleType role, ProjectMemberPositionType position) {
        this.role = role;
        this.position = position;
    }

    public void updateRole(ProjectMemberRoleType role) {
        this.role = role;
    }

    public void updatePosition(ProjectMemberPositionType position) {
        this.position = position;
    }
}


