package fullcare.backend.post.controller;


import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.global.dto.SuccessResponse;
import fullcare.backend.global.errorcode.PostErrorCode;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.DuplicateProjectMemberException;
import fullcare.backend.global.exceptionhandling.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.post.domain.Post;
import fullcare.backend.post.dto.request.PostCreateRequest;
import fullcare.backend.post.dto.request.PostUpdateRequest;
import fullcare.backend.post.dto.response.PostDetailResponse;
import fullcare.backend.post.dto.response.PostListResponse;
import fullcare.backend.post.service.PostService;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.request.ProjectApplyRequest;
import fullcare.backend.project.dto.response.ProjectSimpleListResponse;
import fullcare.backend.project.service.ProjectService;
import fullcare.backend.projectmember.domain.ProjectMemberRole;
import fullcare.backend.projectmember.domain.ProjectMemberRoleType;
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

import java.util.List;


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

        if (!(projectMemberService.validateProjectMember(postCreateRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
        }

        postService.createPost(postCreateRequest, member);
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

        Post post = postService.findPost(postId);
        Project findProject = post.getProject();  // * 프로젝트 정보
        Member author = post.getAuthor(); // * 작성자 정보

        if (!(projectMemberService.validateProjectMember(findProject.getId(), member.getId()))) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
        } else if (!author.getId().equals(member.getId()) || !(projectMemberService.findProjectMember(findProject.getId(), member.getId()).isLeader())) {
            throw new InvalidAccessException(PostErrorCode.INVALID_MODIFY);
        }

        postService.updatePost(postId, postUpdateRequest);
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
                                 @CurrentLoginMember Member member) {

        Post post = postService.findPost(postId);
        Project findProject = post.getProject();  // * 프로젝트 정보
        Member author = post.getAuthor(); // * 작성자 정보

        if (!(projectMemberService.validateProjectMember(findProject.getId(), member.getId()))) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
        } else if (!author.getId().equals(member.getId()) || !(projectMemberService.findProjectMember(findProject.getId(), member.getId()).isLeader())) {
            throw new InvalidAccessException(PostErrorCode.INVALID_DELETE);
        }

        postService.deletePost(postId);
        return new ResponseEntity(HttpStatus.OK);
    }


    // * 특정 모집글 조회
    // todo 좋아요 여부를 응답에 포함시킬 때, 로그인 사용자와 로그인하지 않은 사용자를 어떻게 구분할 것이냐.
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
        } else {
            postDetailResponse = postService.findPostDetailResponse(member.getId(), postId);
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
                                                                 @CurrentLoginMember Member member) { // ? @ModelAttribute가 맞는가
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
        Post post = postService.findPost(postId);

        postService.likePost(post, member);
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
        Post post = postService.findPost(postId);
        Project findProject = post.getProject();  // * 프로젝트 정보

        // ! 이미 프로젝트에 소속된 사람이라면 처리할 필요 없음
        if (projectMemberService.validateProjectMember(findProject.getId(), member.getId())) {
            throw new DuplicateProjectMemberException(ProjectErrorCode.DUPLICATE_PROJECT_MEMBER);
        }

        projectMemberService.addProjectMember(findProject.getId(), member, new ProjectMemberRole(ProjectMemberRoleType.미정, projectApplyRequest.getPosition()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // * 사용자가 속한 프로젝트 목록
    @Operation(method = "get", summary = "사용자 프로젝트 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 프로젝트 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "사용자 프로젝트 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/projectlist")
    public ResponseEntity<SuccessResponse<List<ProjectSimpleListResponse>>> projectlist(@CurrentLoginMember Member member) {
        List<ProjectSimpleListResponse> result = projectService.findSimpleProjectList(member.getId());
        SuccessResponse<List<ProjectSimpleListResponse>> responses = new SuccessResponse<>(result);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

}
