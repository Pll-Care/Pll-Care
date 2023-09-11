package fullcare.backend.evaluation.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FinalCharDto<T> {
    private Long memberId;
    private String name;
    private List<T> evaluation = new ArrayList<>();

    @Builder
    public FinalCharDto(Long memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

    public void addEvaluation(T evaluation) {
        this.evaluation.add(evaluation);
    }
}
