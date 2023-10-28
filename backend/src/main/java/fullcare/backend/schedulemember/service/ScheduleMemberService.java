package fullcare.backend.schedulemember.service;

import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import fullcare.backend.schedulemember.repository.ScheduleMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class ScheduleMemberService {
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final ProjectMemberRepository projectMemberRepository;
    public boolean validateScheduleMember(Long scheduleId, Long projectId, Long memberId) {
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));
        return scheduleMemberRepository.findByScheduleIdAndPmId(scheduleId, projectMember.getId()).isPresent();
    }
}
