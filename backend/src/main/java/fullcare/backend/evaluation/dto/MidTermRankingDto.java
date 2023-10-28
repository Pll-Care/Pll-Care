package fullcare.backend.evaluation.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class MidTermRankingDto {
    private Long rank;
    private String name;
    private Long quantity;
    @Builder
    public MidTermRankingDto(Long rank,  String name, Long quantity) {
        this.rank = rank;
        this.name = name;
        this.quantity = quantity;
    }
}
