package fullcare.backend.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
public class PostUpdateRequest {

    @NotBlank
    @Length(min = 2, max = 20)
    private String title;

    @NotEmpty
    private String reference;

    @NotEmpty
    private String contact;

    @NotEmpty
    private String description;

    @NotEmpty
    private String region;

    // * [모집하는 position : 모집 인원수] 형태의 데이터를 전부 받아야함
    @NotEmpty
    private List<RecruitInfo> recruitInfo;
}
