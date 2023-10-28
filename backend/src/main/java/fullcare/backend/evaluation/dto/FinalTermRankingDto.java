package fullcare.backend.evaluation.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FinalTermRankingDto {
    private Long rank;
    private Long memberId;
    private String name;
    private double score;
    @Builder
    public FinalTermRankingDto(Long rank, Long memberId, String name, double score) {
        this.rank = rank;
        this.memberId = memberId;
        this.name = name;
        this.score = score;
    }
}
