package fullcare.backend.project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ApplyMemberAcceptRequest {

    @NotNull
    private Long postId;

    @NotNull
    private Long memberId;

}
