package fullcare.backend.project.dto.response;

import lombok.Data;

@Data
public class ProjectUpdateResponse {
    private String imageUrl;

    public ProjectUpdateResponse(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
