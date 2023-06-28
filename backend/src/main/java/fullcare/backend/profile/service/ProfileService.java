package fullcare.backend.profile.service;

import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.profile.TechStackResponse;
import fullcare.backend.profile.TechStack;
import fullcare.backend.profile.domain.Profile;
import fullcare.backend.profile.dto.ProjectExperienceResponseDto;
import fullcare.backend.profile.dto.request.ProfileBioUpdateRequest;
import fullcare.backend.profile.dto.request.ProfileUpdateRequest;
import fullcare.backend.profile.dto.response.ProfileResponse;
import jakarta.persistence.EntityNotFoundException;
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
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("해당 사용자 정보가 없습니다."));
        Profile p = findMember.getProfile();
        List<ProjectExperienceResponseDto> projectExperienceRequestDtos = p.getProjectExperiences().stream().map(pro -> ProjectExperienceResponseDto.createResponseDto()
                .projectId(pro.getId())
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
                .projectExperiences(projectExperienceRequestDtos)
                .myProfile(memberId == member.getId())
                .build();
    }

    public void updateProfile(Member member, ProfileUpdateRequest profileUpdateRequest){
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new EntityNotFoundException("해당 사용자 정보가 없습니다."));

        Profile profile = findMember.getProfile();
        profile.updateProfile(profileUpdateRequest);
    }
    public void updateBio(Member member, ProfileBioUpdateRequest profileBioUpdateRequest){
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new EntityNotFoundException("해당 사용자 정보가 없습니다."));
        findMember.getProfile().updateBio(profileBioUpdateRequest.getBio());
    }

    public TechStackResponse findTechStack(String techStack){
        TechStackResponse techStackResponse = new TechStackResponse();
        for(TechStack t : TechStack.values()){
            System.out.println("t.getValue() = " + t.getValue());
            System.out.println("techStack = " + techStack);

            if (t.getValue().toLowerCase().startsWith(techStack.toLowerCase())){
                techStackResponse.addTechStack(t.getValue());
            }
        }
        return techStackResponse;
    }

}
