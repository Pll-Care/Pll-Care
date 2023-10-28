package fullcare.backend.project.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;


@Getter
public class ProjectCreateRequest {

    @NotBlank
    @Length(min = 2, max = 20)
    private String title;

    @NotEmpty
    private String description;

    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;


    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;


    private String imageUrl;


    // ? 테스트 데이터 세팅용으로 사용하는 생성자
    public ProjectCreateRequest(String title, String description, LocalDate startDate, LocalDate endDate, String imageUrl) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
    }
}