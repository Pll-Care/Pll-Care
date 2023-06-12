package fullcare.backend.post.dto.response;

import fullcare.backend.post.dto.request.RecruitInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
public class PostDetailResponse {

    private Long postId;
    private String projectName; // * 프로젝트 이름
    private String memberName; // * 작성자 이름

    private String title;
    private String description;
    private String reference;
    private String contact;
    private String region;
    private String techStack;
//    private boolean isLiked;


    private List<RecruitInfo> recruitInfoList;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public PostDetailResponse(Long postId, String projectName, String memberName, String title,
                              String description, String reference, String contact, String region,
                              String techStack, LocalDateTime createdDate, LocalDateTime modifiedDate) {

        this.postId = postId;
        this.projectName = projectName;
        this.memberName = memberName;
        this.title = title;
        this.description = description;
        this.reference = reference;
        this.contact = contact;
        this.region = region;
        this.techStack = techStack;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public void setRecruitInfoList(List<RecruitInfo> list) {
        this.recruitInfoList = list;
    }

//    public static PostDetailResponse entityToDto(Post post) {
//        return PostDetailResponse.builder()
//                .postId(post.getId())
//                .projectName(post.getProjectMember().getProject().getTitle())
//                .memberName(post.getProjectMember().getMember().getNickname())
//                .title(post.getTitle())
//                .description(post.getDescription())
//                .reference(post.getReference())
//                .contact(post.getContact())
//                .region(post.getRegion())
//                .techStack(post.getTechStack())
//                .recruitInfoList(post.getRecruitments().stream().map(ri -> new RecruitInfo(ri)).toList())
//                .createdDate(post.getCreatedDate())
//                .modifiedDate(post.getModifiedDate())
//                .build();
//    }
}
