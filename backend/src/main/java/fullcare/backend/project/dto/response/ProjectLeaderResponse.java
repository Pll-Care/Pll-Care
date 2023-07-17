package fullcare.backend.project.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProjectLeaderResponse {
    private boolean isLeader;

    @Builder
    public ProjectLeaderResponse(boolean isLeader) {
        this.isLeader = isLeader;
    }
}
