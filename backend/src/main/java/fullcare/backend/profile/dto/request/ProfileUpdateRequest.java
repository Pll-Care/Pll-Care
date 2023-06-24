package fullcare.backend.profile.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import fullcare.backend.post.domain.RecruitPosition;
import fullcare.backend.profile.domain.Contact;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Getter;

import java.time.LocalDate;
@Getter
public class ProfileUpdateRequest {
    private Contact contact;
    private RecruitPosition recruitPosition;
    private String techStack;
    private String title;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;
}
