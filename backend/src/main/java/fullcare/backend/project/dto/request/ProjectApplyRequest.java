package fullcare.backend.project.dto.request;

import fullcare.backend.projectmember.domain.ProjectMemberPositionType;
import lombok.Getter;

@Getter
public class ProjectApplyRequest {

    private ProjectMemberPositionType position;
    
    // ! 테스트 데이터용 생성자
    public ProjectApplyRequest(ProjectMemberPositionType position) {
        this.position = position;
    }
}
