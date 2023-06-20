package fullcare.backend.schedule.domain;


import fullcare.backend.global.State;
import fullcare.backend.project.domain.Project;
import fullcare.backend.projectmember.domain.ProjectMember;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Meeting extends Schedule {

    @Embedded
    private Address address;

    public Meeting(Address address) {
        this.address = address;
    }

    @Builder
    public Meeting(Project project, ProjectMember projectMember, State state, String title, String content, LocalDateTime startDate, LocalDateTime endDate, Address address, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        super(project, projectMember, state, title, content, startDate, endDate, createdDate, modifiedDate);
        this.address = address;
    }

    public void updateAddress(Address address) {
        this.address = address;
    }
}
