package fullcare.backend.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostLikeRequest {

    @NotNull
    private Long postId;
}
