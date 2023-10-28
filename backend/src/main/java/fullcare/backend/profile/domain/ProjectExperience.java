package fullcare.backend.profile.domain;

import fullcare.backend.profile.dto.ProjectExperienceRequestDto;
import fullcare.backend.util.TechStackUtil;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
public class ProjectExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_experience_id")
    private Long id;
    private String title;
    private String description;
    @Column(name = "start_dt")
    private LocalDate startDate;
    @Column(name = "end_dt")
    private LocalDate endDate;
    @Lob
    @Column(name = "project_tech_stack")
    private String techStack;
    @ManyToOne
    @JoinColumn(name="profile_id", nullable = false)
    private Profile profile;
    @Builder
    public ProjectExperience(String title, String description, LocalDate startDate, LocalDate endDate, String techStack, Profile profile) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.techStack = techStack;
        this.profile = profile;
    }
    public void updateTechStack(String techStack){
        this.techStack = techStack;
    }

    public void update(ProjectExperienceRequestDto projectExperienceRequestDto) {
        this.title = projectExperienceRequestDto.getTitle();
        this.description = projectExperienceRequestDto.getDescription();
        this.startDate = projectExperienceRequestDto.getStartDate();
        this.endDate = projectExperienceRequestDto.getEndDate();
        if(projectExperienceRequestDto.getTechStack() != null){
            this.techStack = TechStackUtil.listToString(projectExperienceRequestDto.getTechStack());
        }
    }
}
