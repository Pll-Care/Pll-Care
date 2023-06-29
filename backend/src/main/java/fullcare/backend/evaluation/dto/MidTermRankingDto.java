package fullcare.backend.evaluation.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class MidTermRankingDto {
    private Long rank;
    private Long memberId;
    private String name;
    private Long quantity;
    @Builder
    public MidTermRankingDto(Long rank, Long memberId, String name, Long quantity) {
        this.rank = rank;
        this.memberId = memberId;
        this.name = name;
        this.quantity = quantity;
    }
}
