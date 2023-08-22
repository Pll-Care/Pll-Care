package fullcare.backend.evaluation.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FinalEvalDto {
    private Long memberId;
    private String name;
    private String imageUrl; // ? 사용자 이미지
    private String content;
    private ScoreDto score;

    @Builder
    public FinalEvalDto(Long memberId, String name, String imageUrl, String content, ScoreDto score) {
        this.memberId = memberId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.content = content;
        this.score = score;
    }
}
