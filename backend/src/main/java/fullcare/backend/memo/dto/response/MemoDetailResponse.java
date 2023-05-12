package fullcare.backend.memo.dto.response;

import fullcare.backend.memo.domain.Memo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemoDetailResponse {

    private Long memoId;
    private String author;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public MemoDetailResponse(Long memoId, String author, String title, String content, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.memoId = memoId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static MemoDetailResponse entityToDto(Memo memo) {
        return MemoDetailResponse.builder()
                .memoId(memo.getId())
                .author(memo.getAuthor())
                .title(memo.getTitle())
                .content(memo.getContent())
                .createdDate(memo.getCreatedDate())
                .modifiedDate(memo.getModifiedDate())
                .build();
    }
}
