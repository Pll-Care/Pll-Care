package fullcare.backend.post.domain;

import fullcare.backend.projectmember.domain.ProjectMemberPositionType;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class RecruitInfo {

    private ProjectMemberPositionType position;
    private long currentCnt;

    @Min(value = 1)
    private long totalCnt;

    public RecruitInfo(ProjectMemberPositionType position, long currentCnt, long totalCnt) {
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
