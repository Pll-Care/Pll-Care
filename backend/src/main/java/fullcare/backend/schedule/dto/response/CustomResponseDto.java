package fullcare.backend.schedule.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CustomResponseDto<T>{
    private LocalDate startDate;
    private LocalDate endDate;
    private List<T> custom;

    public CustomResponseDto(LocalDate startDate, LocalDate endDate, List<T> custom) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.custom = custom;
    }
}
