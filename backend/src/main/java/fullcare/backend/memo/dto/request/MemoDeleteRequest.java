package fullcare.backend.memo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MemoDeleteRequest {

    @NotNull
    private Long projectId;

}
