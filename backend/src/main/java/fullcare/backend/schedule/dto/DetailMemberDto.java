package fullcare.backend.schedule.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class DetailMemberDto{
    private Long id;
    private String name;
    private boolean in;
    @Builder
    public DetailMemberDto(Long id, String name, boolean in) {
        this.id = id;
        this.name = name;
        this.in = in;
    }
}