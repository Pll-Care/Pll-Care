package fullcare.backend.evaluation.domain;

import fullcare.backend.evaluation.dto.ScoreDto;
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

    public static double avg(ScoreDto score){
        return (score.getSincerity() + score.getJobPerformance() + score.getPunctuality() + score.getCommunication())/4;
    }
}
