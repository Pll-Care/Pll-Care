package fullcare.backend.post.dto.response;

import fullcare.backend.global.State;
import lombok.Builder;
import lombok.Data;

@Data
public class MyPostResponse {
    private Long postId;
    private String title;
    private String description;
    private State state;
    @Builder
    public MyPostResponse(Long postId, String title, String description, State state) {
        this.postId = postId;
        this.title = title;
        this.description = description;
        this.state = state;
    }
}
