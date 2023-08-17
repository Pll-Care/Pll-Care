package fullcare.backend.project.dto.response;

import lombok.Getter;

@Getter
public class ProjectLeaderResponse {
    private boolean isLeader;

    public ProjectLeaderResponse(boolean isLeader) {
        this.isLeader = isLeader;
    }
}
