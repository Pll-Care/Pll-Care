package fullcare.backend.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostLikeRequest {

    @NotBlank
    private Long postId;
}
