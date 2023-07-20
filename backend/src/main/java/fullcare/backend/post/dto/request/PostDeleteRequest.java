package fullcare.backend.post.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostDeleteRequest {
    
    @NotNull
    private Long projectId;

}
