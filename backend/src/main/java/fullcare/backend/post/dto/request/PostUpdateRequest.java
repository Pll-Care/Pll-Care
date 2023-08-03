package fullcare.backend.post.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import fullcare.backend.post.domain.RecruitInfo;
import fullcare.backend.util.dto.TechStack;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostUpdateRequest {

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

    // * [모집하는 position : 모집 인원수] 형태의 데이터를 전부 받아야함
    @NotEmpty
    private List<RecruitInfo> recruitInfo;
}
