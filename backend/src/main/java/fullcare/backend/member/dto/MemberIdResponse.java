package fullcare.backend.member.dto;

import lombok.Data;

@Data
public class MemberIdResponse {
    private Long id;

    public MemberIdResponse(Long id) {
        this.id = id;
    }
}
