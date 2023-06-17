package fullcare.backend.post.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class PostCreateRequest {

    private Long projectId;
    private String title;
    private String description;
    private String reference;
    private String contact;
    private String region;
    private String techStack;

    // * [모집하는 position : 모집 인원수] 형태의 데이터를 전부 받아야함
    private List<RecruitInfo> recruitInfo;

    public PostCreateRequest(Long projectId, String title, String description, String reference, String contact, String region, String techStack, List<RecruitInfo> recruitInfo) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.reference = reference;
        this.contact = contact;
        this.region = region;
        this.techStack = techStack;
        this.recruitInfo = recruitInfo;
    }
}
