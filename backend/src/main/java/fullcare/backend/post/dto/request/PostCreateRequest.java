package fullcare.backend.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
public class PostCreateRequest {

    @NotBlank
    private Long projectId;

    @NotBlank
    @Length(min = 2, max = 20)
    private String title;

    @NotEmpty
    private String description;

    @NotEmpty
    private String reference;

    @NotEmpty
    private String contact;

    @NotEmpty
    private String region;

    @NotEmpty
    private String techStack;

    // * [모집하는 position : 모집 인원수] 형태의 데이터를 전부 받아야함
    @NotEmpty
    private List<RecruitInfo> recruitInfo;


    // ? 테스트 데이터 세팅용으로 사용하는 생성자
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
