package fullcare.backend.memo.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemoDetailResponse {

    private Long memoId;
    private String author;

    @JsonIgnore
    private Long authorId;

    private String title;
    private String content;
    private boolean isBookmarked;
    private boolean isEditable;
    private boolean isDeletable;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public MemoDetailResponse(Long memoId, String author, Long authorId, String title, String content,
                              boolean isBookmarked, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.memoId = memoId;
        this.author = author;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.isBookmarked = isBookmarked;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public void setDeletable(boolean isDeletable) {
        this.isDeletable = isDeletable;
    }
}
