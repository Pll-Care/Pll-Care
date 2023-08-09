package fullcare.backend.project.dto.request;

import fullcare.backend.projectmember.domain.ProjectMemberPositionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProjectApplyRequest {
    @NotNull
    private ProjectMemberPositionType position;

    // ! 테스트 데이터용 생성자
    public ProjectApplyRequest(ProjectMemberPositionType position) {
        this.position = position;
    }
}
