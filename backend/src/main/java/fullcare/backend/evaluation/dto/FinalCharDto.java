package fullcare.backend.evaluation.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FinalCharDto<T> {
    private Long pmId;
    private String name;
//    private String imageUrl;
    private List<T> evaluation = new ArrayList<>();
    @Builder
    public FinalCharDto(Long pmId, String name) {
        this.pmId = pmId;
        this.name = name;
//        this.imageUrl = imageUrl;
    }

    public void addEvaluation(T evaluation){
        this.evaluation.add(evaluation);
    }
}
