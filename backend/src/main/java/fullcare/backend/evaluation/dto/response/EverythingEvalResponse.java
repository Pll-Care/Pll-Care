package fullcare.backend.evaluation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
public class EverythingEvalResponse<T> {
    private List<T> charts = new ArrayList<>();
    private List<T> ranks = new ArrayList<>();
}
