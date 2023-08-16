package fullcare.backend.post.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fullcare.backend.global.State;
import fullcare.backend.projectmember.domain.ProjectMemberPositionType;
import fullcare.backend.recruitment.domain.RecruitInfo;
import fullcare.backend.util.dto.TechStackDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostDetailResponse {

    private Long postId;

    private Long projectId;
    private String projectTitle; // * 프로젝트 이름
    private String projectImageUrl;

    @JsonIgnore
    private State projectState;

    @JsonIgnore
    private Long authorId;
    private String author; // * 작성자 이름
    private String authorImageUrl;

    private String title;
    private String description;

    private LocalDate recruitStartDate;
    private LocalDate recruitEndDate;

    private String reference;
    private String contact;
    private String region;

    private List<TechStackDto> techStackList = new ArrayList<>();
    @JsonIgnore
    private String techStack;

    private boolean isLiked;
    private boolean isEditable;
    private boolean isDeletable;

    private boolean isAvailable;
    private ProjectMemberPositionType applyPosition;

    private List<RecruitInfo> recruitInfoList = new ArrayList<>();
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public PostDetailResponse(Long postId,
                              Long projectId, String projectTitle, String projectImageUrl, State projectState,
                              Long authorId, String author, String authorImageUrl,
                              String title, String description, LocalDate recruitStartDate, LocalDate recruitEndDate,
                              String reference, String contact, String region, String techStack,
                              boolean isLiked, boolean isEditable, boolean isDeletable,
                              LocalDateTime createdDate, LocalDateTime modifiedDate) {

        this.postId = postId;
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.projectImageUrl = projectImageUrl;
        this.projectState = projectState;
        this.authorId = authorId;
        this.author = author;
        this.authorImageUrl = authorImageUrl;
        this.title = title;
        this.description = description;
        this.recruitStartDate = recruitStartDate;
        this.recruitEndDate = recruitEndDate;
        this.reference = reference;
        this.contact = contact;
        this.region = region;
        this.techStack = techStack;
        this.isLiked = isLiked;
        this.isEditable = isEditable;
        this.isDeletable = isDeletable;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public void setRecruitInfoList(List<RecruitInfo> recruitInfoList) {
        this.recruitInfoList = recruitInfoList;
    }

    public void setTechStackList(List<TechStackDto> techStackList) {
        this.techStackList = techStackList;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setApplyPosition(ProjectMemberPositionType applyPosition) {
        this.applyPosition = applyPosition;
    }
}
