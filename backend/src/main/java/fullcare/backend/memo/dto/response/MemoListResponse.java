package fullcare.backend.memo.dto.response;

import fullcare.backend.memo.domain.Memo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class MemoListResponse {

    private Long memoId;
    private String author;
    private String title;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public MemoListResponse(Long memoId, String author, String title, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.memoId = memoId;
        this.author = author;
        this.title = title;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static MemoListResponse entityToDto(Memo memo) {
        return MemoListResponse.builder()
                .memoId(memo.getId())
                .title(memo.getTitle())
                .author(memo.getAuthor())
                .createdDate(memo.getCreatedDate())
                .modifiedDate(memo.getModifiedDate())
                .build();
    }
}
