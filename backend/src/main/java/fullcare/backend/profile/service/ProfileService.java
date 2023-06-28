package fullcare.backend.profile.service;

import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.profile.domain.Profile;
import fullcare.backend.profile.dto.ProjectExperienceDto;
import fullcare.backend.profile.dto.request.ProfileUpdateRequest;
import fullcare.backend.profile.dto.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ProfileService {
    private final MemberRepository memberRepository;
    @Transactional(readOnly = true)
    public ProfileResponse findProfile(Long memberId, Member member){
        Member findMember = memberRepository.findById(memberId).orElseThrow();
        Profile p = findMember.getProfile();
        List<ProjectExperienceDto> projectExperienceDtos = p.getProjectExperiences().stream().map(pro -> ProjectExperienceDto.builder()
                .title(pro.getTitle())
                .description(pro.getDescription())
                .startDate(pro.getStartDate())
                .endDate(pro.getEndDate())
                .techStack(pro.getTechStack()).build()).collect(Collectors.toList());
        return ProfileResponse.builder()
                .bio(p.getBio())
                .contact(p.getContact())
                .recruitPosition(p.getRecruitPosition())
                .techStack(p.getTechStack())
                .projectExperiences(projectExperienceDtos)
                .myProfile(memberId == member.getId())
                .build();
    }

    public void updateProfile(Member member, ProfileUpdateRequest profileUpdateRequest){
        Member findMember = memberRepository.findById(member.getId()).orElseThrow();
        Profile profile = findMember.getProfile();
        profile.updateProfile(profileUpdateRequest);
    }

}
