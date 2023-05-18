package fullcare.backend.schedule.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String name;
    @Builder
    public MemberDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
