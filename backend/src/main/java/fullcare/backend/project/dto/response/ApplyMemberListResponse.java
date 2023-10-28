package fullcare.backend.project.dto.response;

import fullcare.backend.projectmember.domain.ProjectMemberPositionType;
import lombok.Builder;
import lombok.Getter;


@Getter
public class ApplyMemberListResponse {

    private Long applyId;

    // ! 아래의 두개는 지울지말지 보류
//    private Long postId;
    private Long memberId;

    private String name;
    private String imageUrl;
    private ProjectMemberPositionType position;


    @Builder
    public ApplyMemberListResponse(Long applyId, Long postId, Long memberId, String name, String imageUrl, ProjectMemberPositionType position) {
        this.applyId = applyId;
//        this.postId = postId;
        this.memberId = memberId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.position = position;
    }
}
