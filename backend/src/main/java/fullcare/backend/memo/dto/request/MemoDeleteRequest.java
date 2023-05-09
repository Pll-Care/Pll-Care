package fullcare.backend.memo.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class MemoDeleteRequest {

    @NotEmpty
    private Long projectId;

    @NotEmpty
    private Long memoId;
}
