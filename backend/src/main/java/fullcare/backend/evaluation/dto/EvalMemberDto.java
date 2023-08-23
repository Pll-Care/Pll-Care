package fullcare.backend.evaluation.dto;

import fullcare.backend.member.domain.Member;
import fullcare.backend.projectmember.domain.ProjectMember;
import lombok.Builder;
import lombok.Data;

@Data
public class EvalMemberDto {
    private Long id;
    private String name;
    @Builder
    public EvalMemberDto(ProjectMember projectMember) {
        this.id = projectMember.getMember().getId();
        this.name = projectMember.getMember().getName();
    }
}
