package fullcare.backend.project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ApplyMemberRejectRequest {

    @NotNull
    private Long applyId;
    
}
