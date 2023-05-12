package fullcare.backend.schedulemember.domain;

import fullcare.backend.member.domain.Member;
import fullcare.backend.project.domain.Project;
import fullcare.backend.projectmember.domain.ProjectMemberRole;
import fullcare.backend.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "schedule_member")
@Table(name = "schedule_member")
public class ScheduleMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Builder
    public ScheduleMember(Member member, Schedule schedule) {
        this.member = member;
        this.schedule = schedule;
    }
}