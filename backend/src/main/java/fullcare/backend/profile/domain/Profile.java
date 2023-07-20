package fullcare.backend.profile.domain;

import fullcare.backend.member.domain.Member;
import fullcare.backend.post.domain.RecruitPosition;
import fullcare.backend.profile.dto.ProjectExperienceRequestDto;
import fullcare.backend.profile.dto.request.ProfileUpdateRequest;
import fullcare.backend.util.TechStackUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

//import javax.json.JsonMergePatch;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "profile")
@Entity
@DynamicUpdate
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @OneToOne(mappedBy = "profile")
    private Member member;
    @Length(min = 5, max = 25)
    private String bio; //* 한줄 자기소개
    @Embedded
    private Contact contact;
    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private RecruitPosition recruitPosition;
    @Lob
    @Column(name = "tech_stack")
    private String techStack;
    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProjectExperience> projectExperiences = new ArrayList<>();

    public Profile(String bio) {
        this.bio = bio;
    }

    public void updateBio(String bio) {
        this.bio = bio;
    }

    public void updateProfile(ProfileUpdateRequest profileUpdateRequest) {

        this.contact = profileUpdateRequest.getContact();
        this.recruitPosition = profileUpdateRequest.getRecruitPosition();
        this.techStack = TechStackUtil.listToString(profileUpdateRequest.getTechStack());
        if (profileUpdateRequest.getProjectId() != null && profileUpdateRequest.isDelete()) {
            if (profileUpdateRequest.getTechStack() != null) {
                this.techStack = TechStackUtil.listToString(profileUpdateRequest.getTechStack());
            }
            if (profileUpdateRequest.getProjectId() != null && profileUpdateRequest.isDelete()) {
                ProjectExperience projectExperience = this.projectExperiences.stream().filter(pe -> pe.getId() == profileUpdateRequest.getProjectId())
                        .findFirst().orElseThrow(() -> new EntityNotFoundException("프로젝트 경험이 없습니다."));
                projectExperiences.remove(projectExperience);
            } else {
                if (profileUpdateRequest.getProjectExperiences() != null) {
                    List<ProjectExperienceRequestDto> peList = profileUpdateRequest.getProjectExperiences();
                    for (ProjectExperienceRequestDto peDto : peList) {

                        ProjectExperience projectExperience = ProjectExperience.builder()
                                .title(peDto.getTitle())
                                .description(peDto.getDescription())
                                .startDate(peDto.getStartDate())
                                .endDate(peDto.getEndDate())
                                .techStack(TechStackUtil.listToString(peDto.getTechStack()))
                                .profile(this)
                                .build();
                        this.projectExperiences.add(projectExperience);
                    }
                }
            }
        }
    }

}
