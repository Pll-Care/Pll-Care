package fullcare.backend.post.controller;


import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.global.errorcode.GlobalErrorCode;
import fullcare.backend.global.errorcode.PostErrorCode;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.DuplicateProjectMemberException;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.global.exceptionhandling.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.post.domain.Post;
import fullcare.backend.post.dto.request.PostCreateRequest;
import fullcare.backend.post.dto.request.PostDeleteRequest;
import fullcare.backend.post.dto.request.PostUpdateRequest;
import fullcare.backend.post.dto.response.PostDetailResponse;
import fullcare.backend.post.dto.response.PostListResponse;
import fullcare.backend.post.service.PostService;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.request.ProjectApplyRequest;
import fullcare.backend.project.service.ProjectService;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.service.ProjectMemberService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@Tag(name = "모집글", description = "모집글 관련 API")
@RequestMapping("/api/auth/post")
@RestController
public class PostController {

    private final PostService postService;
    private final ProjectMemberService projectMemberService;
    private final ProjectService projectService;


    // * 새로운 모집글 생성
    @Operation(method = "post", summary = "모집글 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "모집글 생성 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "모집글 생성 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity create(@RequestBody PostCreateRequest postCreateRequest,
                                 @CurrentLoginMember Member member) {

        ProjectMember findProjectMember = projectService.isProjectAvailable(postCreateRequest.getProjectId(), member.getId(), false);

        postService.createPost(findProjectMember, postCreateRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // * 특정 모집글 수정
    @Operation(method = "put", summary = "모집글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모집글 수정 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "모집글 수정 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{postId}")
    public ResponseEntity update(@PathVariable Long postId,
                                 @RequestBody PostUpdateRequest postUpdateRequest,
                                 @CurrentLoginMember Member member) {

        ProjectMember findProjectMember = projectService.isProjectAvailable(postUpdateRequest.getProjectId(), member.getId(), false);
        Post findPost = postService.findPostWithRecruitments(postId);

        if (findPost.getAuthor().getId() != findProjectMember.getMember().getId() && !findProjectMember.isLeader()) {
            throw new InvalidAccessException(PostErrorCode.INVALID_MODIFY);
        }

        postService.updatePost(findPost.getId(), postUpdateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }


    // * 특정 모집글 삭제
    @Operation(method = "delete", summary = "모집글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모집글 삭제 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "모집 삭제 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity delete(@PathVariable Long postId,
                                 @RequestBody PostDeleteRequest postDeleteRequest,
                                 @CurrentLoginMember Member member) {

        ProjectMember findProjectMember = projectService.isProjectAvailable(postDeleteRequest.getProjectId(), member.getId(), false);
        Post findPost = postService.findPost(postId);

        if (findPost.getAuthor().getId() != findProjectMember.getMember().getId() && !findProjectMember.isLeader()) {
            throw new InvalidAccessException(PostErrorCode.INVALID_MODIFY);
        }

        postService.deletePost(findPost.getId());
        return new ResponseEntity(HttpStatus.OK);
    }


    // * 특정 모집글 조회
    @Operation(method = "get", summary = "모집글 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모집글 단건 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PostDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "모집글 단건 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{postId}")
    public ResponseEntity<?> details(@PathVariable Long postId,
                                     @CurrentLoginMember Member member) {

        PostDetailResponse postDetailResponse;

        if (member == null) {
            postDetailResponse = postService.findPostDetailResponse(null, postId);
            postDetailResponse.setAvailable(true);
        } else {
            postDetailResponse = postService.findPostDetailResponse(member.getId(), postId);

            // ! 지원정보가 이미 존재한다면 지원할 수 없음
            postDetailResponse.setAvailable(projectMemberService.findApplyMember(postDetailResponse.getPostId(), member.getId()).isEmpty());
        }


        return new ResponseEntity<>(postDetailResponse, HttpStatus.OK);
    }


    // * 모집글 목록 조회
    @Operation(method = "get", summary = "모집글 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모집글 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "모집글 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/list")
    public ResponseEntity<CustomPageImpl<PostListResponse>> list(@ModelAttribute CustomPageRequest pageRequest,
                                                                 @CurrentLoginMember Member member) {
        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        CustomPageImpl<PostListResponse> responses;

        if (member == null) {
            responses = postService.findPostList(null, pageable);
        } else {
            responses = postService.findPostList(member.getId(), pageable);
        }

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // * 특정 모집글 좋아요
    @Operation(method = "post", summary = "특정 모집글 좋아요")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모집글 좋아요 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "모집글 좋아요 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{postId}/like")
    public ResponseEntity like(@PathVariable Long postId, @CurrentLoginMember Member member) {
        Post findPost = postService.findPost(postId);

        postService.likePost(findPost, member);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // * 특정 게시글을 통해 해당 프로젝트에 지원
    @Operation(method = "post", summary = "특정 모집글을 통해 프로젝트 지원")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 지원 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 지원 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{postId}/apply")
    public ResponseEntity apply(@PathVariable Long postId, @RequestBody ProjectApplyRequest projectApplyRequest, @CurrentLoginMember Member member) {

        Post findPost = postService.findPostWithRecruitments(postId);
        Project findProject = findPost.getProject();  // * 프로젝트 정보

        try {
            ProjectMember findProjectMember = projectMemberService.findProjectMember(findProject.getId(), member.getId());

            // * 위의 코드에서 EntityNotFound를 던지지 않으면 이미 프로젝트에 소속된 인원이므로 지원할 수 없음
            throw new DuplicateProjectMemberException(ProjectErrorCode.DUPLICATE_PROJECT_MEMBER); // todo 이미 프로젝트에 소속된 사용자입니다.

        } catch (EntityNotFoundException e) {
            postService.applyProjectByPost(findPost, member, projectApplyRequest.getPosition());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // * 특정 게시글을 통해 지원한 프로젝트 지원 취소
    @Operation(method = "post", summary = "프로젝트 지원 취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 지원 취소 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 지원 취소 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{postId}/applycancel")
    public ResponseEntity applyCancel(@PathVariable Long postId, @CurrentLoginMember Member member) {

        Post findPost = postService.findPost(postId);

        // ! 이미 프로젝트에 소속된 사용자가 지원 취소 API를 호출했을 때, 지원 정보 없음 처리 OR 비정상적 접근 처리?
        Project findProject = findPost.getProject();  // * 프로젝트 정보

        try {
            ProjectMember findProjectMember = projectMemberService.findProjectMember(findProject.getId(), member.getId());

            // * 위의 코드에서 EntityNotFound를 던지지 않으면 이미 프로젝트에 소속된 인원이므로 지원취소 시 정상 처리할 수 없음
            throw new InvalidAccessException(GlobalErrorCode.INVALID_ACCESS); // todo 비정상적인 API 호출 -> 이미 프로젝트에 소속된 사용자입니다. 소속된 사용자는 탈퇴 혹은 강퇴에 의해 프로젝트에서 나갈 수 있음.

        } catch (EntityNotFoundException e) {
            postService.cancelApply(findPost, member);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
