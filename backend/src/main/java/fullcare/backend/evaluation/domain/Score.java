package fullcare.backend.evaluation.domain;

import fullcare.backend.evaluation.dto.ScoreDto;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.ToString;

@Embeddable
@Data
public class Score {
    private int sincerity;
    private int jobPerformance;
    private int punctuality;
    private int communication;

    public static double avg(ScoreDto score){
        return (score.getSincerity() + score.getJobPerformance() + score.getPunctuality() + score.getCommunication())/4;
    }

    public static boolean valid(Score score){
        if(score.getSincerity()>5||score.getJobPerformance()>5||score.getPunctuality()>5||score.getCommunication()>5){
            return false;
        }
        if(score.getSincerity()<0||score.getJobPerformance()<0||score.getPunctuality()<0||score.getCommunication()<0){
            return false;
        }
        return true;
    }
}
