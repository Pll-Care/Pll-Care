package fullcare.backend.profile.domain;

import fullcare.backend.profile.dto.request.ProfileUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Embeddable
@NoArgsConstructor
public class ProjectExperience {
    private String title;
    private String description;
    @Column(name = "start_dt")
    private LocalDate startDate;
    @Column(name = "end_dt")
    private LocalDate endDate;
    @Lob
    @Column(name = "project_tech_stack")
    private String techStack;

    public void updateProjectExperience(ProfileUpdateRequest profileUpdateRequest){
        this.title = profileUpdateRequest.getTitle();
        this.description = profileUpdateRequest.getDescription();
        this.startDate = profileUpdateRequest.getStartDate();
        this.endDate = profileUpdateRequest.getEndDate();
        this.techStack = profileUpdateRequest.getTechStack();
    }

}
