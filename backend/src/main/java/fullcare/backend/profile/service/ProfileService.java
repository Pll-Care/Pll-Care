package fullcare.backend.profile.service;

import fullcare.backend.global.errorcode.MemberErrorCode;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.profile.TechStack;
import fullcare.backend.profile.TechStackResponse;
import fullcare.backend.profile.domain.Profile;
import fullcare.backend.profile.dto.ProjectExperienceDto;
import fullcare.backend.profile.dto.ProjectExperienceResponseDto;
import fullcare.backend.profile.dto.request.ProfileBioUpdateRequest;
import fullcare.backend.profile.dto.request.ProfileUpdateRequest;
import fullcare.backend.profile.dto.response.ProfileBioResponse;
import fullcare.backend.profile.dto.response.ProfileContactResponse;
import fullcare.backend.profile.dto.response.ProfileImageResponse;
import fullcare.backend.profile.dto.response.ProfileProjectExperienceResponse;
import fullcare.backend.profile.dto.response.ProfileTechStackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ProfileService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public ProfileBioResponse findBio(Long memberId, Member member) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
        Profile p = findMember.getProfile();
        return ProfileBioResponse.builder()
                .name(findMember.getName())
                .nickName(findMember.getNickname())
                .imageUrl(findMember.getImageUrl())
                .bio(p.getBio())
                .myProfile(memberId == member.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public ProfileContactResponse findContact(Long memberId, Member member) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
        Profile p = findMember.getProfile();
        return new ProfileContactResponse(p.getContact(), memberId == member.getId());
    }

    @Transactional(readOnly = true)
    public ProfileTechStackResponse findRoleAndTechStack(Long memberId, Member member) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
        Profile p = findMember.getProfile();
        List<String> techStacks = null;
        if(p.getTechStack() != null && !p.getTechStack().isEmpty()) {
            techStacks = Arrays.stream(p.getTechStack().split(",")).collect(Collectors.toList());
        }
        return new ProfileTechStackResponse(p.getRecruitPosition(), techStacks, memberId == member.getId());
    }

    @Transactional(readOnly = true)
    public ProfileProjectExperienceResponse findProjectExperience(Long memberId, Member member) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
        Profile p = findMember.getProfile();
        List<ProjectExperienceDto> ProjectExperienceDtos = p.getProjectExperiences().stream().map(pro -> ProjectExperienceDto.createResponseDto()
//                .projectId(pro.getId())
                .title(pro.getTitle())
                .description(pro.getDescription())
                .startDate(pro.getStartDate())
                .endDate(pro.getEndDate())
                .techStack(pro.getTechStack()).build()).collect(Collectors.toList());
        List<ProjectExperienceResponseDto> data = new ArrayList<>();
        List<ProjectExperienceResponseDto> equalList = null;
        for (ProjectExperienceDto dto : ProjectExperienceDtos) {
            int year = dto.getStartDate().getYear();
            equalList = data.stream().filter(d -> d.getYear() == year).collect(Collectors.toList());
            if(equalList.size()>0) {
                equalList.get(0).getProjectExperiences().add(dto);
            }else{
                List<ProjectExperienceDto> projectExperienceDtos = new ArrayList<>();
                projectExperienceDtos.add(dto);
                data.add(new ProjectExperienceResponseDto(dto.getStartDate().getYear(),
                    projectExperienceDtos));
            }
        }
        return ProfileProjectExperienceResponse.builder()
                .data(data)
                .myProfile(memberId == member.getId())
                .build();
    }

    public void updateProfile(Member member, ProfileUpdateRequest profileUpdateRequest) {
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
        Profile profile = findMember.getProfile();
        profile.updateProfile(profileUpdateRequest);
    }

    public void updateBio(Member member, ProfileBioUpdateRequest profileBioUpdateRequest) {
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
        findMember.getProfile().updateBio(profileBioUpdateRequest.getBio());
    }


    public ProfileImageResponse findProfileImage(Member member) {
        Member findMember = memberRepository.findById(member.getId()).orElseThrow();
        return new ProfileImageResponse(findMember.getId(), findMember.getImageUrl());
    }

}
