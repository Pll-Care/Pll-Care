package fullcare.backend.memo.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoIdResponse {

    private Long memoId;

    public MemoIdResponse(Long memoId) {
        this.memoId = memoId;
    }
}
