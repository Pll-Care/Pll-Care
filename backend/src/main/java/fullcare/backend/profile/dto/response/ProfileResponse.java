package fullcare.backend.profile.dto.response;

import fullcare.backend.post.domain.RecruitPosition;
import fullcare.backend.profile.domain.Contact;
import fullcare.backend.profile.dto.ProjectExperienceRequestDto;
import fullcare.backend.profile.dto.ProjectExperienceResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ProfileResponse {
    private String bio; //* 한줄 자기소개
    private Contact contact;
    private RecruitPosition recruitPosition;
    private String techStack;
    private List<ProjectExperienceResponseDto> projectExperiences;

    private boolean myProfile;

    @Builder
    public ProfileResponse(String bio, Contact contact, RecruitPosition recruitPosition, String techStack, List<ProjectExperienceResponseDto> projectExperiences, boolean myProfile) {
        this.bio = bio;
        this.contact = contact;
        this.recruitPosition = recruitPosition;
        this.techStack = techStack;
        this.projectExperiences = projectExperiences;
        this.myProfile = myProfile;
    }
}
