package fullcare.backend.profile.domain;

import fullcare.backend.member.domain.Member;
import fullcare.backend.post.domain.RecruitPosition;
import fullcare.backend.profile.dto.request.ProfileUpdateRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "profile")
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @OneToOne(mappedBy = "profile")
    private Member member;
    private String bio; //* 한줄 자기소개
    @Embedded
    private Contact contact;
    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private RecruitPosition recruitPosition;
    @Lob
    @Column(name = "tech_stack")
    private String techStack;
    @Embedded
    private ProjectExperience projectExperience;

    public Profile(String bio) {
        this.bio = bio;
    }
    public void updateProfile(ProfileUpdateRequest profileUpdateRequest){
        this.contact = profileUpdateRequest.getContact();
        this.recruitPosition = profileUpdateRequest.getRecruitPosition();
        this.techStack = profileUpdateRequest.getTechStack();
        this.projectExperience.updateProjectExperience(profileUpdateRequest);
    }
}
