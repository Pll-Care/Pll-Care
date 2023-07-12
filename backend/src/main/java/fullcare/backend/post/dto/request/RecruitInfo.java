package fullcare.backend.post.dto.request;

import fullcare.backend.post.domain.RecruitPosition;
import fullcare.backend.post.domain.Recruitment;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class RecruitInfo {
    private RecruitPosition position;
    private long currentCnt;
    private long totalCnt;

    public RecruitInfo(RecruitPosition position, long currentCnt, long totalCnt) {
        this.position = position;
        this.currentCnt = currentCnt;
        this.totalCnt = totalCnt;
    }


    public RecruitInfo(Recruitment recruitment) {
        this.position = recruitment.getRecruitPosition();
        this.currentCnt = recruitment.getCurrentAmount();
        this.totalCnt = recruitment.getTotalAmount();
    }
}
