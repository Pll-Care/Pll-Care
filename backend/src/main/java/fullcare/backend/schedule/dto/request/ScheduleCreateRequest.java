package fullcare.backend.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import fullcare.backend.schedule.ScheduleCategory;
import fullcare.backend.schedule.domain.Address;
import fullcare.backend.schedule.dto.MemberDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScheduleCreateRequest {
    @NotNull
    private Long projectId;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startDate;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endDate;
    @NotNull
    private ScheduleCategory category;
    @NotNull
    private List<MemberDto> memberDtos;
    @NotNull
    private String title;
    @NotNull
    private String content;

    private Address address;

}
