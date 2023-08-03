package fullcare.backend.post.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fullcare.backend.recruitment.domain.RecruitInfo;
import fullcare.backend.util.dto.TechStackDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class PostListResponse {

    private Long postId;
    private String projectName; // * 프로젝트 이름
    private String projectImageUrl;

    private String title;

    private LocalDate recruitStartDate;
    private LocalDate recruitEndDate;

    private List<TechStackDto> techStackList = new ArrayList<>();
    @JsonIgnore
    private String techStack;

    private boolean isLiked;

    private List<RecruitInfo> recruitInfoList;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public PostListResponse(Long postId, String projectName, String projectImageUrl, String title,
                            LocalDate recruitStartDate, LocalDate recruitEndDate,
                            String techStack, boolean isLiked,
                            LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.postId = postId;
        this.projectName = projectName;
        this.projectImageUrl = projectImageUrl;
        this.title = title;
        this.recruitStartDate = recruitStartDate;
        this.recruitEndDate = recruitEndDate;
        this.techStack = techStack;
        this.isLiked = isLiked;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public void setRecruitInfoList(List<RecruitInfo> recruitInfoList) {
        this.recruitInfoList = recruitInfoList;
    }

    public void setTechStackList(List<TechStackDto> techStackList) {
        this.techStackList = techStackList;
    }
}
