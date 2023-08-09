package fullcare.backend.main.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MostLikedPostResponse {

    private Long postId;

    private Long projectId;
    private String projectTitle;
    private String projectImageUrl;

    private LocalDate recruitEndDate;

    private String title;
    private String description;

    private int likeCount;

    @Builder
    public MostLikedPostResponse(Long postId, Long projectId, String projectTitle, String projectImageUrl, LocalDate recruitEndDate, String title, String description, int likeCount) {
        this.postId = postId;
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.projectImageUrl = projectImageUrl;
        this.recruitEndDate = recruitEndDate;
        this.title = title;
        this.description = description;
        this.likeCount = likeCount;
    }
}
