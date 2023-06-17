package fullcare.backend.evaluation.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChartDto<T> {
    private Long memberId;
    private String name;
//    private String imageUrl;
    private List<T> evaluation = new ArrayList<>();
    @Builder
    public ChartDto(Long memberId, String name) {
        this.memberId = memberId;
        this.name = name;
//        this.imageUrl = imageUrl;
    }

    public void addEvaluation(T evaluation){
        this.evaluation.add(evaluation);
    }
}
