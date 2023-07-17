package fullcare.backend.post.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fullcare.backend.post.domain.Post;
import fullcare.backend.post.dto.request.RecruitInfo;
import fullcare.backend.util.dto.TechStack;
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

    public PostListResponse(Post post, boolean isLiked) {
        this.postId = post.getId();
        this.projectName = post.getProject().getTitle();
        this.projectImageUrl = post.getProject().getImageUrl();
        this.title = post.getTitle();
        this.recruitStartDate = post.getRecruitStartDate();
        this.recruitEndDate = post.getRecruitEndDate();
        this.techStack = post.getTechStack();
        this.isLiked = isLiked;
        this.createdDate = post.getCreatedDate();
        this.modifiedDate = post.getModifiedDate();
    }

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

        for (TechStack t : TechStack.values()) {
            if (t.getValue().toLowerCase().startsWith(techStack.toLowerCase())) {
                this.techStackList.add(new TechStackDto(t.getValue(), null));
            }
        }

        this.isLiked = isLiked;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static PostListResponse entityToDto(Post post) {
        return PostListResponse.builder()
                .postId(post.getId())
                .projectName(post.getProject().getTitle())
                .projectImageUrl(post.getProject().getImageUrl())
                .title(post.getTitle())
                .techStack(post.getTechStack())
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .build();
    }

    public void setRecruitInfoList(List<RecruitInfo> list) {
        this.recruitInfoList = list;
    }
}
