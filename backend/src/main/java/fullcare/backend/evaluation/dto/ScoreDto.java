package fullcare.backend.evaluation.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScoreDto {
    private double sincerity;
    private double jobPerformance;
    private double punctuality;
    private double communication;
    @Builder
    public ScoreDto(double sincerity, double jobPerformance, double punctuality, double communication) {
        this.sincerity = sincerity;
        this.jobPerformance = jobPerformance;
        this.punctuality = punctuality;
        this.communication = communication;
    }
}
