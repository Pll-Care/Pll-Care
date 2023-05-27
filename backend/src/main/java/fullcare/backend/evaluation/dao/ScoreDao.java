package fullcare.backend.evaluation.dao;

import lombok.Data;

@Data
public class ScoreDao {
    private Long id;
    private double sincerity;
    private double jobPerformance;
    private double punctuality;
    private double communication;

    public ScoreDao(Long id, double sincerity, double jobPerformance, double punctuality, double communication) {
        this.id = id;
        this.sincerity = sincerity;
        this.jobPerformance = jobPerformance;
        this.punctuality = punctuality;
        this.communication = communication;
    }
}
