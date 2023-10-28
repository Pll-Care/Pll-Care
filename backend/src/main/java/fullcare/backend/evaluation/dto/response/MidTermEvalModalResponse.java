package fullcare.backend.evaluation.dto.response;

import fullcare.backend.evaluation.dto.BadgeDto;
import fullcare.backend.evaluation.dto.EvalMemberDto;
import fullcare.backend.global.State;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class MidTermEvalModalResponse {
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private State state;
    List<EvalMemberDto> members = new ArrayList<>();
    List<BadgeDto> badgeDtos = new ArrayList<>();
    @Builder
    public MidTermEvalModalResponse(String title, LocalDateTime startDate, LocalDateTime endDate, State state) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
    }
    public void addMember(EvalMemberDto evalMemberDto){
        this.members.add(evalMemberDto);
    }
    public void addBadge(BadgeDto badgeDto){
        this.badgeDtos.add(badgeDto);
    }
}
