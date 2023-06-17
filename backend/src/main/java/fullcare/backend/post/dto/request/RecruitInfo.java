package fullcare.backend.post.dto.request;

import fullcare.backend.post.domain.RecruitPosition;
import fullcare.backend.post.domain.Recruitment;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class RecruitInfo {
    private RecruitPosition position;
    private long cnt;

    public RecruitInfo(RecruitPosition position, long cnt) {
        this.position = position;
        this.cnt = cnt;
    }

    public RecruitInfo(Recruitment recruitment) {
        position = recruitment.getRecruitPosition();
        cnt = recruitment.getAmount();
    }
}
