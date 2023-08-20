package fullcare.backend.schedule.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String name;
    private String imageUrl;

    @Builder
    public MemberDto(Long id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
