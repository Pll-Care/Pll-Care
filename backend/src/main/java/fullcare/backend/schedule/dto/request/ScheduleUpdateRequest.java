package fullcare.backend.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fullcare.backend.global.State;
import fullcare.backend.schedule.ScheduleCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScheduleUpdateRequest {

    @NotNull
    private Long projectId;

    @NotNull
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @JsonSerialize
    private LocalDateTime startDate;

    @NotNull
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @JsonSerialize
    private LocalDateTime endDate;

    // ? NotBlank인가 NotEmpty인가?
    @NotNull
    private State state;

    @NotNull
    private ScheduleCategory category;


    @NotNull
    private List<Long> pmIds;

    @NotBlank
    @Length(min = 2, max = 20)
    private String title;

    @NotEmpty
    private String content;

    private String address;
}
