package fullcare.backend.schedule.domain;


import fullcare.backend.global.State;
import fullcare.backend.project.domain.Project;
import jakarta.persistence.Embedded;
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

public class Meeting extends Schedule{

    public Meeting(Address address) {
        this.address = address;
    }

    @Embedded
    private Address address;
    @Builder
    public Meeting(Project project, String author, State state, String title, String content, LocalDateTime startDate, LocalDateTime endDate, Address address) {
        super(project, author, state, title, content, startDate, endDate);
        this.address = address;
    }
    public void updateAddress(Address address) {
        this.address = address;
    }
}
