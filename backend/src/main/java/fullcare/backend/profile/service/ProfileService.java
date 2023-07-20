package fullcare.backend.profile.service;

import fullcare.backend.global.errorcode.MemberErrorCode;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.profile.domain.Profile;
import fullcare.backend.profile.domain.ProjectExperience;
import fullcare.backend.profile.dto.ProjectExperienceDto;
import fullcare.backend.profile.dto.ProjectExperienceResponseDto;
import fullcare.backend.profile.dto.request.ProfileBioUpdateRequest;
import fullcare.backend.profile.dto.request.ProfileUpdateRequest;
import fullcare.backend.profile.dto.response.*;
import fullcare.backend.s3.S3Service;
import fullcare.backend.util.TechStackUtil;
import fullcare.backend.util.dto.TechStack;
import fullcare.backend.util.dto.TechStackDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional
public class ProfileService {
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public ProfileBioResponse findBio(Long memberId, Member member) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
        Profile p = findMember.getProfile();
        Long id = member == null ? null : member.getId();
        return ProfileBioResponse.builder()
                .name(findMember.getName())
                .nickName(findMember.getNickname())
                .imageUrl(findMember.getImageUrl())
                .bio(p.getBio())
                .myProfile(memberId == id)
                .build();
    }

    @Transactional(readOnly = true)
    public ProfileContactResponse findContact(Long memberId, Member member) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
        Profile p = findMember.getProfile();
        Long id = member == null ? null : member.getId();

        return new ProfileContactResponse(p.getContact(), memberId == id);
    }

    @Transactional(readOnly = true)
    public ProfileTechStackResponse findRoleAndTechStack(Long memberId, Member member) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
        Profile p = findMember.getProfile();
        List<TechStackDto> techStackDtos = new ArrayList<>();
        if (p.getTechStack() != null && !p.getTechStack().isEmpty()) {
            List<TechStack> techStacks = TechStackUtil.stringToList(p.getTechStack());
            for (TechStack t : techStacks) {
                techStackDtos.add(new TechStackDto(t.getValue(), s3Service.find(t.getValue(), t.getContentType())));
            }
        }
        Long id = member == null ? null : member.getId();
        return new ProfileTechStackResponse(p.getRecruitPosition(), techStackDtos, memberId == id);
    }

    @Transactional(readOnly = true)
    public ProfileProjectExperienceResponse findProjectExperience(Long memberId, Member member) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
        Profile p = findMember.getProfile();

        List<ProjectExperienceDto> ProjectExperienceDtos = new ArrayList<>();
        for (ProjectExperience pro : p.getProjectExperiences()) {
            String techStack = pro.getTechStack();
            List<TechStackDto> techStackDtos = new ArrayList<>();
            if (pro.getTechStack() != null && !pro.getTechStack().isEmpty()) {
                List<TechStack> techStacks = TechStackUtil.stringToList(techStack);
                for (TechStack t : techStacks) {
                    techStackDtos.add(new TechStackDto(t.getValue(), s3Service.find(t.getValue(), t.getContentType())));
                }
            }
            ProjectExperienceDto projectExperienceDto = ProjectExperienceDto.createResponseDto()
                    .projectId(pro.getId())
                    .title(pro.getTitle())
                    .description(pro.getDescription())
                    .startDate(pro.getStartDate())
                    .endDate(pro.getEndDate())
                    .techStack(techStackDtos).build();
            ProjectExperienceDtos.add(projectExperienceDto);
        }

        List<ProjectExperienceResponseDto> data = new ArrayList<>();
        List<ProjectExperienceResponseDto> equalList = null;
        for (ProjectExperienceDto dto : ProjectExperienceDtos) {
            int year = dto.getStartDate().getYear();
            equalList = data.stream().filter(d -> d.getYear() == year).collect(Collectors.toList());
            if (equalList.size() > 0) {
                equalList.get(0).getProjectExperiences().add(dto);
            } else {
                List<ProjectExperienceDto> projectExperienceDtos = new ArrayList<>();
                projectExperienceDtos.add(dto);
                data.add(new ProjectExperienceResponseDto(dto.getStartDate().getYear(),
                        projectExperienceDtos));
            }
        }
        Long id = member == null ? null : member.getId();
        return ProfileProjectExperienceResponse.builder()
                .data(data)
                .myProfile(memberId == id)
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
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
        return new ProfileImageResponse(findMember.getId(), findMember.getImageUrl());
    }

}
