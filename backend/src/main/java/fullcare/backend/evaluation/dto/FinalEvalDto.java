package fullcare.backend.evaluation.dto;

import fullcare.backend.evaluation.domain.Score;
import lombok.Builder;
import lombok.Data;

@Data
public class FinalEvalDto {
    private Long memberId;
    private String memberName;
    private String imageUrl; // ? 사용자 이미지
    private String content;
//    private Score score;
    @Builder
    public FinalEvalDto(Long memberId, String memberName, String imageUrl, String content) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.imageUrl = imageUrl;
        this.content = content;
//        this.score = score;
    }
}
