package fullcare.backend.memo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class MemoCreateRequest {

    @NotNull
    private Long projectId;

    @NotBlank
    @Length(min = 2, max = 20)
    private String title;

    @NotEmpty
    private String content;

    public MemoCreateRequest(Long projectId, String title, String content) {
        this.projectId = projectId;
        this.title = title;
        this.content = content;
    }
}
