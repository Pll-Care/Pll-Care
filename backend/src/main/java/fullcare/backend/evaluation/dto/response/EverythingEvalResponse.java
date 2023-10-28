package fullcare.backend.evaluation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
public class EverythingEvalResponse<T, S> {
    private List<T> charts = new ArrayList<>();
    private List<S> ranks = new ArrayList<>();
    private boolean evaluation;
}
