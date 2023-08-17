package fullcare.backend.memo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // * -> RequestDto를 만드는 과정에서 필드가 1개뿐이면, Default 생성자가 필요함.
public class MemoBookmarkRequest {

    @NotNull
    private Long projectId;

    // ! 테스트 데이터를 위한 생성자
    public MemoBookmarkRequest(Long projectId) {
        this.projectId = projectId;
    }
}
