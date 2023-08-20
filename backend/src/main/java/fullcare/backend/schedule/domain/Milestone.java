package fullcare.backend.schedule.domain;


import fullcare.backend.global.State;
import fullcare.backend.member.domain.Member;
import fullcare.backend.project.domain.Project;
import fullcare.backend.projectmember.domain.ProjectMember;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity

public class Milestone extends Schedule{
    @Builder
    public Milestone(Project project, ProjectMember author, State state, String title, String content, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        super(project, author, state, title, content, startDate, endDate, createdDate, modifiedDate);
    }
}
