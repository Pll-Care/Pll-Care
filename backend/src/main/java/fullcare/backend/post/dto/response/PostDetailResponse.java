package fullcare.backend.post.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fullcare.backend.post.domain.Post;
import fullcare.backend.post.dto.request.RecruitInfo;
import fullcare.backend.util.TechStackUtil;
import fullcare.backend.util.dto.TechStackDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
public class PostDetailResponse {

    private Long postId;
    private String projectName; // * 프로젝트 이름
    private String projectImageUrl;

    private String author; // * 작성자 이름
    private String authorImageUrl;

    private String title;
    private String description;

    private LocalDate recruitStartDate;
    private LocalDate recruitEndDate;

    private String reference;
    private String contact;
    private String region;

    private List<TechStackDto> techStackDtoList = new ArrayList<>();

    @JsonIgnore
    private String techStack;

    private boolean isLiked;
    private boolean isEditable;
    private boolean isDeletable;


    private List<RecruitInfo> recruitInfoList = new ArrayList<>();
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public PostDetailResponse(Long postId, String projectName, String projectImageUrl, String author, String authorImageUrl,
                              String title, String description, LocalDate recruitStartDate, LocalDate recruitEndDate,
                              String reference, String contact, String region, String techStack,
                              boolean isLiked, boolean isEditable, boolean isDeletable,
                              LocalDateTime createdDate, LocalDateTime modifiedDate) {

        this.postId = postId;
        this.projectName = projectName;
        this.projectImageUrl = projectImageUrl;
        this.author = author;
        this.authorImageUrl = authorImageUrl;
        this.title = title;
        this.description = description;
        this.recruitStartDate = recruitStartDate;
        this.recruitEndDate = recruitEndDate;
        this.reference = reference;
        this.contact = contact;
        this.region = region;
        this.techStackDtoList = TechStackUtil.stringToList(techStack).stream().map(t -> new TechStackDto(t.getValue(), null)).collect(Collectors.toList());
        this.isLiked = isLiked;
        this.isEditable = isEditable;
        this.isDeletable = isDeletable;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static PostDetailResponse entityToDto(Post post) {
        return PostDetailResponse.builder()
                .postId(post.getId())
                .projectName(post.getProject().getTitle())
                .projectImageUrl(post.getProject().getImageUrl())
                .author(post.getAuthor().getNickname())
                .authorImageUrl(post.getAuthor().getImageUrl())
                .title(post.getTitle())
                .description(post.getDescription())
                .reference(post.getReference())
                .contact(post.getContact())
                .region(post.getRegion())
                .techStack(post.getTechStack())
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .build();
    }

    public void setRecruitInfoList(List<RecruitInfo> list) {
        this.recruitInfoList = list;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public void setDeletable(boolean deletable) {
        isDeletable = deletable;
    }
}
