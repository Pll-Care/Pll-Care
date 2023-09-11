package fullcare.backend.post.controller;


import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.member.domain.Member;
import fullcare.backend.post.dto.request.PostCreateRequest;
import fullcare.backend.post.dto.request.PostUpdateRequest;
import fullcare.backend.post.dto.response.PostDetailResponse;
import fullcare.backend.post.dto.response.PostListResponse;
import fullcare.backend.post.service.PostService;
import fullcare.backend.project.dto.request.ProjectApplyRequest;
import fullcare.backend.security.jwt.CurrentLoginMember;
import fullcare.backend.util.CustomPageImpl;
import fullcare.backend.util.CustomPageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    // * 새로운 모집글 생성
    @Operation(method = "post", summary = "모집글 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "모집글 생성 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "모집글 생성 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity create(@RequestBody PostCreateRequest postCreateRequest,
                                 @CurrentLoginMember Member member) {

        postService.createPost(member.getId(), postCreateRequest);
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

        postService.updatePost(member.getId(), postId, postUpdateRequest);
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

        postService.deletePost(member.getId(), postId);
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

        postService.likePost(member.getId(), postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // * 특정 게시글을 통해 해당 프로젝트에 지원
    @Operation(method = "post", summary = "특정 모집글을 통해 프로젝트 지원")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 지원 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 지원 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{postId}/apply")
    public ResponseEntity apply(@PathVariable Long postId, @Valid @RequestBody ProjectApplyRequest projectApplyRequest, @CurrentLoginMember Member member) {

        postService.applyProjectByPost(member.getId(), postId, projectApplyRequest);
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

        postService.cancelApply(member.getId(), postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
