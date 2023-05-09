package fullcare.backend.memo.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class MemoUpdateRequest {


    @NotEmpty
    private Long projectId;

    @NotEmpty
    private Long memoId;

    @NotEmpty
    @Length(min = 2, max = 20)
    private String title;

    @NotEmpty
    private String content;

}
