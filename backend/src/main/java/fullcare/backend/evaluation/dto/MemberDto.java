package fullcare.backend.evaluation.dto;

import fullcare.backend.member.domain.Member;
import lombok.Builder;
import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String name;
    @Builder
    public MemberDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
    }
}
