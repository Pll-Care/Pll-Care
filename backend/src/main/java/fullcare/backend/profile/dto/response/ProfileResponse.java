package fullcare.backend.profile.dto.response;

import fullcare.backend.post.domain.RecruitPosition;
import fullcare.backend.profile.domain.Contact;
import fullcare.backend.profile.domain.ProjectExperience;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
public class ProfileResponse {
    private String bio; //* 한줄 자기소개
    private Contact contact;
    private RecruitPosition recruitPosition;
    private String techStack;
    private ProjectExperience projectExperience;
    private boolean myProfile;
    @Builder
    public ProfileResponse(String bio, Contact contact, RecruitPosition recruitPosition, String techStack, ProjectExperience projectExperience, boolean myProfile) {
        this.bio = bio;
        this.contact = contact;
        this.recruitPosition = recruitPosition;
        this.techStack = techStack;
        this.projectExperience = projectExperience;
        this.myProfile = myProfile;
    }
}
