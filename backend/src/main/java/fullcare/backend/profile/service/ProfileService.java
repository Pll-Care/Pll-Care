package fullcare.backend.profile.service;

import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.profile.domain.Profile;
import fullcare.backend.profile.dto.request.ProfileUpdateRequest;
import fullcare.backend.profile.dto.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ProfileService {
    private final MemberRepository memberRepository;
    @Transactional(readOnly = true)
    public ProfileResponse findProfile(Long memberId, Member member){
        Member findMember = memberRepository.findById(memberId).orElseThrow();
        Profile p = findMember.getProfile();
        return ProfileResponse.builder()
                .bio(p.getBio())
                .contact(p.getContact())
                .recruitPosition(p.getRecruitPosition())
                .techStack(p.getTechStack())
                .projectExperience(p.getProjectExperience())
                .myProfile(memberId == member.getId())
                .build();
    }
    @Transactional
    public void updateProfile(Member member, ProfileUpdateRequest profileUpdateRequest){
        Member findMember = memberRepository.findById(member.getId()).orElseThrow();
        Profile p = findMember.getProfile();

    }

}
