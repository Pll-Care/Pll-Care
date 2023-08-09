package fullcare.backend.memo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoBookmarkRequest {


    @NotNull
    private Long projectId;

    // ! 테스트 데이터를 위한 생성자
    public MemoBookmarkRequest(Long projectId) {
        this.projectId = projectId;
    }
}
