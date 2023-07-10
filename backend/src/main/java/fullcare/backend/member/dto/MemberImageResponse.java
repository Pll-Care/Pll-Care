package fullcare.backend.member.dto;

import lombok.Data;

@Data
public class MemberImageResponse {
    private String imageUrl;

    public MemberImageResponse(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
