package fullcare.backend.project.dto;

import lombok.Getter;

@Getter
public class ProjectCreateRequest {
    private String title;
    private String content;

    public ProjectCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
