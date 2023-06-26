package fullcare.backend.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import fullcare.backend.global.State;
import fullcare.backend.schedule.ScheduleCategory;
import fullcare.backend.schedule.domain.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScheduleUpdateRequest {

    @NotBlank
    private Long projectId;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    // ? NotBlank인가 NotEmpty인가?
    @NotNull
    private State state;

    @NotNull
    private ScheduleCategory category;


    @NotNull
    private List<Long> memberIds;

    @NotBlank
    @Length(min = 2, max = 20)
    private String title;

    @NotEmpty
    private String content;

    @NotNull
    private Address address;
}
