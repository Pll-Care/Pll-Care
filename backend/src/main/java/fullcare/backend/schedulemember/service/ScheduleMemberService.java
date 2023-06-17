package fullcare.backend.schedulemember.service;

import fullcare.backend.schedulemember.repository.ScheduleMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class ScheduleMemberService {
    private final ScheduleMemberRepository scheduleMemberRepository;
    public boolean validateScheduleMember(Long scheduleId, Long memberId) {
        return scheduleMemberRepository.findByScheduleIdAndMemberId(scheduleId, memberId).isPresent();
    }
}
