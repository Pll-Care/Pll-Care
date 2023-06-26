package fullcare.backend.memo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class MemoCreateRequest {

    @NotBlank
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
