package fullcare.backend.profile.controller;

import fullcare.backend.evaluation.dto.response.MyEvalChartResponse;
import fullcare.backend.evaluation.dto.response.MyEvalDetailResponse;
import fullcare.backend.evaluation.dto.response.MyEvalListResponse;
import fullcare.backend.evaluation.service.EvaluationService;
//import fullcare.backend.global.dto.FailureResponse;
import fullcare.backend.global.State;
import fullcare.backend.global.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.post.dto.response.MyPostResponse;
import fullcare.backend.post.service.PostService;
import fullcare.backend.profile.dto.request.ProfileUpdateRequest;
import fullcare.backend.profile.dto.response.ProfileResponse;
import fullcare.backend.profile.service.ProfileService;
import fullcare.backend.project.dto.response.ProjectMemberListResponse;
import fullcare.backend.projectmember.service.ProjectMemberService;
import fullcare.backend.schedule.service.ScheduleService;
import fullcare.backend.security.jwt.CurrentLoginMember;
import fullcare.backend.util.CustomPageImpl;
import fullcare.backend.util.CustomPageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "개인 페이지", description = "개인 페이지 관련 API")
@RequestMapping("/api/auth/profile/{memberId}")
@RestController
public class ProfileController {
    private final ProfileService profileService;
    private final PostService postService;
    private final EvaluationService evaluationService;
    private final ProjectMemberService projectMemberService;
    // * 특정 프로젝트의 멤버 목록
    @Operation(method = "get", summary = "개인 프로필 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "개인 프로필 조회 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "개인 프로필 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ProfileResponse> findProfile(@PathVariable Long memberId, @CurrentLoginMember Member member) {
        ProfileResponse response = profileService.findProfile(memberId, member);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "put", summary = "개인 프로필 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "개인 프로필 수정 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "개인 프로필 수정 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @PutMapping
    public ResponseEntity projectMemberList(@PathVariable Long memberId, @CurrentLoginMember Member member, @RequestBody ProfileUpdateRequest profileUpdateRequest) {

        return new ResponseEntity<>(HttpStatus.OK);
    }



    //* 모집글 api
    @Operation(method = "get", summary = "개인페이지 작성한 모집글 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "개인페이지 작성한 모집글 조회 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "개인페이지 작성한 모집글 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/post")
    public ResponseEntity<CustomPageImpl<MyPostResponse>> findMyPost(@PathVariable Long memberId, @RequestParam(value = "state", defaultValue = "TBD")State state, CustomPageRequest pageRequest, @CurrentLoginMember Member member) {
        if(memberId != member.getId()){
            throw new InvalidAccessException("해당 프로필 권한이 없습니다.");
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
        if(memberId != member.getId()){
            throw new InvalidAccessException("해당 프로필 권한이 없습니다.");
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
        return new ResponseEntity(response,HttpStatus.OK);
    }
    @Operation(method = "get", summary = "개인페이지 프로젝트 평가 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "개인페이지 프로젝트 평가 조회 성공", responseCode = "200",useReturnTypeSchema = true)
    })
    @GetMapping("/evaluation/{projectId}")
    public ResponseEntity<MyEvalDetailResponse> findMyEvalDetail(@PathVariable Long memberId, @PathVariable Long projectId, @CurrentLoginMember Member member) {
//        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
//            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
//        }
        MyEvalDetailResponse response = evaluationService.findMyEval(projectId, memberId);

        return new ResponseEntity(response,HttpStatus.OK);
    }
    @Operation(method = "get", summary = "개인페이지 평가 차트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "개인페이지 평가 차트 조회 성공", responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/evaluation/chart")
    public ResponseEntity<MyEvalChartResponse> findMyEvalChart(@PathVariable Long memberId, @CurrentLoginMember Member member) {
        MyEvalChartResponse response = evaluationService.findMyEvalChart(memberId);
        return new ResponseEntity(response,HttpStatus.OK);
    }
}
