package fullcare.backend.profile.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class ProfileBioResponse {
    private String name;
    private String nickName;
    private String imageUrl;
    private String bio; //* 한줄 자기소개
    private boolean myProfile;
    @Builder
    public ProfileBioResponse(String name, String nickName, String imageUrl, String bio, boolean myProfile) {
        this.name = name;
        this.nickName = nickName;
        this.imageUrl = imageUrl;
        this.bio = bio;
        this.myProfile = myProfile;
    }
}
