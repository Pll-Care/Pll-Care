package fullcare.backend.project.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectApplyResponse extends ProjectMemberListResponse{
    private Long pmId;
    @Builder(builderMethodName = "applyList")
    public ProjectApplyResponse(Long id, Long pmId, String name, String imageUrl, String position) {
        super(id, name, imageUrl, position);
        this.pmId = pmId;
    }
}
