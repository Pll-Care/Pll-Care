package fullcare.backend.schedule.domain;


import fullcare.backend.global.State;
import fullcare.backend.project.domain.Project;
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
    public Milestone(Project project, String author, State state, String title, String content, LocalDateTime startDate, LocalDateTime endDate) {
        super(project, author, state, title, content, startDate, endDate);
    }
}
