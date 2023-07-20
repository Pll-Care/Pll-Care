package fullcare.backend.post.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import fullcare.backend.post.domain.RecruitInfo;
import fullcare.backend.util.dto.TechStack;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostCreateRequest {

    @NotNull
    private Long projectId;

    @NotBlank
    @Length(min = 2, max = 20)
    private String title;

    @NotEmpty
    private String description;

    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate recruitStartDate;

    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate recruitEndDate;

    @NotEmpty
    private String reference;

    @NotEmpty
    private String contact;

    @NotEmpty
    private String region;

    @NotEmpty
    private List<TechStack> techStack = new ArrayList<>();

    @NotEmpty
    private List<RecruitInfo> recruitInfo;


    // ? 테스트 데이터 세팅용으로 사용하는 생성자
    public PostCreateRequest(Long projectId, String title, String description, String reference, String contact, String region, List<RecruitInfo> recruitInfo) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.reference = reference;
        this.contact = contact;
        this.region = region;
        this.recruitInfo = recruitInfo;
    }
}
