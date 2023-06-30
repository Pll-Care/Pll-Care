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
    private boolean isBookmarked;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public MemoDetailResponse(Long memoId, String author, String title, String content, boolean isBookmarked, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.memoId = memoId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.isBookmarked = isBookmarked;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static MemoDetailResponse entityToDto(Memo memo, boolean isBookmarked) {
        return MemoDetailResponse.builder()
                .memoId(memo.getId())
                .author(memo.getAuthor().getNickname())
                .title(memo.getTitle())
                .content(memo.getContent())
                .isBookmarked(isBookmarked)
                .createdDate(memo.getCreatedDate())
                .modifiedDate(memo.getModifiedDate())
                .build();
    }
}
