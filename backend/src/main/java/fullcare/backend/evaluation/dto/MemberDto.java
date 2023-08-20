package fullcare.backend.evaluation.dto;

import fullcare.backend.projectmember.domain.ProjectMember;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberDto {

    private Long id;
    private String name;

    @Builder
    public MemberDto(ProjectMember projectMember) {
        this.id = projectMember.getMember().getId();
        this.name = projectMember.getMember().getName();
    }
}
