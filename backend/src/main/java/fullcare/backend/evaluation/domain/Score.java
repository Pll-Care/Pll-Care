package fullcare.backend.evaluation.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.ToString;

@Embeddable
@Data
public class Score {
    private double sincerity;
    private double jobPerformance;
    private double punctuality;
    private double communication;
}
