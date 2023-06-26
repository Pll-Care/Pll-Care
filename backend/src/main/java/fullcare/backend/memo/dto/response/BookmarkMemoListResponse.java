package fullcare.backend.memo.dto.response;

import fullcare.backend.memo.domain.BookmarkMemo;
import fullcare.backend.memo.domain.Memo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BookmarkMemoListResponse {

    private Long memoId;
    private String author;
    private String title;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private boolean isBookmarked;

    @Builder
    public BookmarkMemoListResponse(Long memoId, String author, String title,
                                    LocalDateTime createdDate, LocalDateTime modifiedDate,
                                    boolean isBookmarked) {
        this.memoId = memoId;
        this.author = author;
        this.title = title;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.isBookmarked = isBookmarked;
    }

    public static BookmarkMemoListResponse entityToDto(BookmarkMemo bookmarkMemo) {
        Memo memo = bookmarkMemo.getMemo();

        return BookmarkMemoListResponse.builder()
                .memoId(memo.getId())
                .title(memo.getTitle())
                .author(memo.getAuthor())
                .createdDate(memo.getCreatedDate())
                .modifiedDate(memo.getModifiedDate())
                .isBookmarked(true)
                .build();
    }


}
