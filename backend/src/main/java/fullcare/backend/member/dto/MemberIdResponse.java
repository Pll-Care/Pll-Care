package fullcare.backend.member.dto;

import lombok.Data;

@Data
public class MemberIdResponse {
    private Long memberId;

    public MemberIdResponse(Long memberId) {
        this.memberId = memberId;
    }
}
