package fullcare.backend.memo.dto.response;

import fullcare.backend.memo.domain.BookmarkMemo;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BookmarkMemoListResponse {

    private MemoListResponse memoListResponse;
    private boolean isBookmarked;


    @Builder
    public BookmarkMemoListResponse(MemoListResponse memoListResponse, boolean isBookmarked) {
        this.memoListResponse = memoListResponse;
        this.isBookmarked = isBookmarked;
    }

    public static BookmarkMemoListResponse entityToDto(BookmarkMemo bookmarkMemo) {
        MemoListResponse memoListResponse = MemoListResponse.entityToDto(bookmarkMemo.getMemo());

        return BookmarkMemoListResponse.builder()
                .memoListResponse(memoListResponse)
                .isBookmarked(bookmarkMemo.isBookmarked())
                .build();
    }
}
