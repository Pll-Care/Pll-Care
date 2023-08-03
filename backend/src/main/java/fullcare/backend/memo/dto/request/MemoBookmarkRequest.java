package fullcare.backend.memo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MemoBookmarkRequest {

    @NotNull
    private Long projectId;

    // ! 테스트 데이터를 위한 생성자
    public MemoBookmarkRequest(@NotNull Long projectId) {
        this.projectId = projectId;
    }
}
