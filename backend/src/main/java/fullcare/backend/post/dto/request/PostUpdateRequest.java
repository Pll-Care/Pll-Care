package fullcare.backend.post.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class PostUpdateRequest {

    private String title;
    private String reference;
    private String contact;
    private String description;
    private String region;

    // * [모집하는 position : 모집 인원수] 형태의 데이터를 전부 받아야함
    private List<RecruitInfo> recruitInfo;
}
