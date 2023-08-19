package fullcare.backend.evaluation.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class MidTermRankingDto {
    private Long rank;
    private Long pmId;
    private String name;
    private Long quantity;
    @Builder
    public MidTermRankingDto(Long rank, Long pmId, String name, Long quantity) {
        this.rank = rank;
        this.pmId = pmId;
        this.name = name;
        this.quantity = quantity;
    }
}
