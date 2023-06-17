package fullcare.backend.memo.dto.response;

import lombok.Getter;

@Getter
public class MemoIdResponse {

    private Long memoId;

    public MemoIdResponse(Long memoId) {
        this.memoId = memoId;
    }
}
