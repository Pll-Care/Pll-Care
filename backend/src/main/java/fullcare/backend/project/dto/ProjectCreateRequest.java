package fullcare.backend.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProjectCreateRequest {
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;
    public ProjectCreateRequest(String title, String content, LocalDate startDate, LocalDate endDate) {

            this.title = title;
            this.content = content;
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }