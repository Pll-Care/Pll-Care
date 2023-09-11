package fullcare.backend.evaluation.dto;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MidChartDto {
    private Long memberId;
    private String name;
    private Map<EvaluationBadge, Long> evaluation = new HashMap();
    @Builder
    public MidChartDto(Long memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

}
