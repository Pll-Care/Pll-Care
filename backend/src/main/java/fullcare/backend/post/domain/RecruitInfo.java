package fullcare.backend.post.domain;

import lombok.Getter;

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
