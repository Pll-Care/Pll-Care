package fullcare.backend.evaluation.dto;

import lombok.Data;

@Data
public class ScoreDao {
    private Long memberId;
    private double sincerity;
    private double jobPerformance;
    private double punctuality;
    private double communication;

    public ScoreDao(Long memberId, double sincerity, double jobPerformance, double punctuality, double communication) {
        this.memberId = memberId;
        this.sincerity = sincerity;
        this.jobPerformance = jobPerformance;
        this.punctuality = punctuality;
        this.communication = communication;
    }
}
