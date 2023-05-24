package fullcare.backend.evaluation.dto;

import lombok.Data;

@Data
public class MidTermRankingDto {
    private int rank;
    private Long memberId;
    private String name;
    private int quantity;
}
