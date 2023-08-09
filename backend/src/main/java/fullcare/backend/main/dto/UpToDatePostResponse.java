package fullcare.backend.main.dto;

import lombok.Builder;

import java.time.LocalDate;

public class UpToDatePostResponse {

    private Long postId;

    private Long projectId;
    private String projectTitle;
    private String projectImageUrl;

    private LocalDate recruitEndDate;

    private String title;
    private String description;


    @Builder
    public UpToDatePostResponse(Long postId, Long projectId, String projectTitle, String projectImageUrl, LocalDate recruitEndDate, String title, String description) {
        this.postId = postId;
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.projectImageUrl = projectImageUrl;
        this.recruitEndDate = recruitEndDate;
        this.title = title;
        this.description = description;
    }
}
