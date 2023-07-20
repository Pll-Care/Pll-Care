package fullcare.backend.memo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MemoBookmarkRequest {

    @NotNull
    private Long projectId;
    
}
