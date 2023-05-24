package fullcare.backend.evaluation.dto;

import fullcare.backend.evaluation.domain.EvaluationBadge;
import fullcare.backend.evaluation.dto.response.MidtermDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class Badge implements BadgeInterface {
    private EvaluationBadge evaluationBadge;
    private Long quantity;

    public static void setBadges(List<BadgeInterface> badges){
        List<EvaluationBadge> evaluationBadges = new ArrayList<>();
        evaluationBadges.add(EvaluationBadge.탁월한_리더);
        evaluationBadges.add(EvaluationBadge.열정적인_참여자);
        evaluationBadges.add(EvaluationBadge.최고의_서포터);
        evaluationBadges.add(EvaluationBadge.아이디어_뱅크);
        for (BadgeInterface b: badges) {

            Badge badge = (Badge) b;
            System.out.println("badge.getEvaluationBadge() = " + badge.getEvaluationBadge());
            if(badge.getEvaluationBadge().equals(EvaluationBadge.탁월한_리더)){
                evaluationBadges.remove(EvaluationBadge.탁월한_리더);
            }else if(badge.getEvaluationBadge().equals(EvaluationBadge.열정적인_참여자)){
                evaluationBadges.remove(EvaluationBadge.열정적인_참여자);
            }else if(badge.getEvaluationBadge().equals(EvaluationBadge.최고의_서포터)){
                evaluationBadges.remove(EvaluationBadge.최고의_서포터);
            }else if(badge.getEvaluationBadge().equals(EvaluationBadge.아이디어_뱅크)){
                evaluationBadges.remove(EvaluationBadge.아이디어_뱅크);
            }
        }
        for (EvaluationBadge evaluationBadge : evaluationBadges) { // 없는 뱃지 추가
            System.out.println("evaluationBadge 추가 = " + evaluationBadge);
            badges.add(new Badge(evaluationBadge,0l));
        }
    }

}
