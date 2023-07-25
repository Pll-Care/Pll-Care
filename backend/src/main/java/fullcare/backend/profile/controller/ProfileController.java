package fullcare.backend.profile.controller;

import fullcare.backend.evaluation.dto.response.MyEvalChartResponse;
import fullcare.backend.evaluation.dto.response.MyEvalDetailResponse;
import fullcare.backend.evaluation.dto.response.MyEvalListResponse;
import fullcare.backend.evaluation.service.EvaluationService;
import fullcare.backend.global.State;
import fullcare.backend.global.errorcode.MemberErrorCode;
import fullcare.backend.global.exceptionhandling.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.post.dto.response.MyPostResponse;
import fullcare.backend.post.service.PostService;
import fullcare.backend.profile.dto.request.ProfileBioUpdateRequest;
import fullcare.backend.profile.dto.request.ProfileUpdateRequest;
import fullcare.backend.profile.dto.response.*;
import fullcare.backend.profile.service.ProfileService;
import fullcare.backend.project.service.ProjectService;
import fullcare.backend.projectmember.service.ProjectMemberService;
import fullcare.backend.security.jwt.CurrentLoginMember;
import fullcare.backend.util.CustomPageImpl;
import fullcare.backend.util.CustomPageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import javax.json.JsonMergePatch;
//import javax.json.JsonPatch;


@RequiredArgsConstructor
@Tag(name = "개인 페이지", description = "개인 페이지 관련 API")
@RequestMapping("/api/auth/profile/{memberId}")
@RestController
public class ProfileController {
    private final ProfileService profileService;
    private final PostService postService;
    private final EvaluationService evaluationService;
    private final ProjectService projectService;

    // * 개인 프로필 api 조회
    @Operation(method = "get", summary = "한 줄 소개 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "한 줄 소개 조회 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "한 줄 소개 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/bio")
    public ResponseEntity<ProfileBioResponse> findBio(@PathVariable Long memberId, @CurrentLoginMember Member member) {
        ProfileBioResponse response = profileService.findBio(memberId, member);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "연락처 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "연락처 조회 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "연락처 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/contact")
    public ResponseEntity<ProfileContactResponse> findContact(@PathVariable Long memberId, @CurrentLoginMember Member member) {
        ProfileContactResponse response = profileService.findContact(memberId, member);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "직무, 기술스택 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "직무, 기술스택 조회 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "직무, 기술스택 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/roletechstack")
    public ResponseEntity<ProfileTechStackResponse> findRoleAndTechStack(@PathVariable Long memberId, @CurrentLoginMember Member member) {
        ProfileTechStackResponse response = profileService.findRoleAndTechStack(memberId, member);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "프로젝트 경험 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 경험 조회 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "프로젝트 경험 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/experience")
    public ResponseEntity<ProfileProjectExperienceResponse> findProjectExperience(@PathVariable Long memberId, @CurrentLoginMember Member member) {
        ProfileProjectExperienceResponse response = profileService.findProjectExperience(memberId, member);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "내 프로필 검증")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 프로필 검증 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "내 프로필 검증 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/validate")
    public ResponseEntity<ValidateMyProfileResponse> validateMyProfile(@PathVariable Long memberId, @CurrentLoginMember Member member) {
        return new ResponseEntity<>(new ValidateMyProfileResponse(memberId == member.getId()), HttpStatus.OK);
    }


    // * 개인 프로필 api 생성, 수정, 삭제
    @Operation(method = "patch", summary = "개인 프로필 생성, 수정, 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "개인 프로필 생성, 수정, 삭제 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "개인 프로필 생성, 수정, 삭제 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @PatchMapping
    public ResponseEntity updateProfile(@PathVariable Long memberId, @CurrentLoginMember Member member, @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        if (memberId != member.getId()) {
            throw new InvalidAccessException(MemberErrorCode.MEMBER_PROFILE_INVALID_ACCESS);
        }
        profileService.updateProfile(member, profileUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(method = "put", summary = "개인 프로필 한 줄 소개 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "개인 프로필 한 줄 소개 수정 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "개인 프로필 한 줄 소개 수정 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @PutMapping
    public ResponseEntity updateBio(@PathVariable Long memberId, @CurrentLoginMember Member member, @Valid @RequestBody ProfileBioUpdateRequest profileBioUpdateRequest) {
        if (memberId != member.getId()) {
            throw new InvalidAccessException(MemberErrorCode.MEMBER_PROFILE_INVALID_ACCESS);
        }
        profileService.updateBio(member, profileBioUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    //* 모집글 api
    @Operation(method = "get", summary = "개인페이지 작성한 모집글 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "개인페이지 작성한 모집글 조회 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "개인페이지 작성한 모집글 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/post")
    public ResponseEntity<CustomPageImpl<MyPostResponse>> findMyPost(@PathVariable Long memberId, @RequestParam(value = "state", defaultValue = "TBD") State state, CustomPageRequest pageRequest, @CurrentLoginMember Member member) {
        if (memberId != member.getId()) {
            throw new InvalidAccessException(MemberErrorCode.MEMBER_PROFILE_INVALID_ACCESS);
        }
        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        CustomPageImpl<MyPostResponse> response = postService.findMyPost(member.getId(), state, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(method = "get", summary = "개인페이지 좋아요 한 모집글 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "개인페이지 좋아요 한 모집글 조회 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "개인페이지 좋아요 한 모집글 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/post/like")
    public ResponseEntity<CustomPageImpl<MyPostResponse>> findMyLikePost(@PathVariable Long memberId, CustomPageRequest pageRequest, @CurrentLoginMember Member member) {
        if (memberId != member.getId()) {
            throw new InvalidAccessException(MemberErrorCode.MEMBER_PROFILE_INVALID_ACCESS);
        }
        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        CustomPageImpl<MyPostResponse> response = postService.findMyLikePost(member.getId(), pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    //* 평가 api
    @Operation(method = "get", summary = "개인페이지 프로젝트 평가 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "개인페이지 프로젝트 평가 리스트 조회 성공", responseCode = "200", useReturnTypeSchema = true)
    })// * 개인 페이지
    @GetMapping("/evaluation")
    public ResponseEntity<CustomPageImpl<MyEvalListResponse>> findMyEvalList(@PathVariable Long memberId, CustomPageRequest pageRequest, @CurrentLoginMember Member member) {
        PageRequest of = pageRequest.of("project");
        Pageable pageable = (Pageable) of;
        Page<MyEvalListResponse> response = evaluationService.findMyEvalList(pageable, memberId);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "개인페이지 프로젝트 평가 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "개인페이지 프로젝트 평가 조회 성공", responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/evaluation/{projectId}")
    public ResponseEntity<MyEvalDetailResponse> findMyEvalDetail(@PathVariable Long memberId, @PathVariable Long projectId, @CurrentLoginMember Member member) {
        projectService.isProjectAvailable(projectId, memberId, true);
        MyEvalDetailResponse response = evaluationService.findMyEval(projectId, memberId);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "개인페이지 평가 차트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "개인페이지 평가 차트 조회 성공", responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/evaluation/chart")
    public ResponseEntity<MyEvalChartResponse> findMyEvalChart(@PathVariable Long memberId, @CurrentLoginMember Member member) {
        MyEvalChartResponse response = evaluationService.findMyEvalChart(memberId);
        return new ResponseEntity(response, HttpStatus.OK);
    }



}
