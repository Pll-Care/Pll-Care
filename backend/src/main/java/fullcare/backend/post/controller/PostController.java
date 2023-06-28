package fullcare.backend.post.controller;


import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.global.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.post.domain.Post;
import fullcare.backend.post.dto.request.PostCreateRequest;
import fullcare.backend.post.dto.request.PostUpdateRequest;
import fullcare.backend.post.dto.response.MyPostResponse;
import fullcare.backend.post.dto.response.PostDetailResponse;
import fullcare.backend.post.dto.response.PostListResponse;
import fullcare.backend.post.service.PostService;
import fullcare.backend.project.domain.Project;
import fullcare.backend.projectmember.DuplicateProjectMemberException;
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
import org.springframework.data.domain.Page;
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

    // todo 구현해야하는 기능
    // * 2. 모집글 목록 조회(페이지 + 필터링)

    // * 4. 모집글 좋아요
    // * 5. 모집글을 통해 프로젝트에 지원

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
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        }

        postService.createPost(postCreateRequest, member.getId());
        return new ResponseEntity(HttpStatus.OK);
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
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        } else if (!author.getId().equals(member.getId()) || !(projectMemberService.findProjectMember(findProject.getId(), member.getId()).isLeader())) {
            throw new InvalidAccessException("해당 모집글의 수정 권한이 없습니다.");
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
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        } else if (!author.getId().equals(member.getId()) || !(projectMemberService.findProjectMember(findProject.getId(), member.getId()).isLeader())) {
            throw new InvalidAccessException("해당 모집글의 삭제 권한이 없습니다.");
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

        PostDetailResponse postDetailResponse = postService.findPostDetailResponse(postId);
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
        CustomPageImpl<PostListResponse> responses=null;
        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        if(member!=null){
        responses = postService.findPostList(member.getId(), pageable);}
        else{  responses = postService.findPostList(null, pageable);}

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
    // todo path variable 대신 requestparam으로 넣는게 나을지도
    @Operation(method = "post", summary = "특정 모집글을 통해 프로젝트 지원")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 지원 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 지원 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{postId}/apply")
    public ResponseEntity apply(@PathVariable Long postId, @CurrentLoginMember Member member) {
        Post post = postService.findPost(postId);
        Project findProject = post.getProject();  // * 프로젝트 정보

        // ! 이미 프로젝트에 소속된 사람이라면 처리할 필요 없음
        if (projectMemberService.validateProjectMember(findProject.getId(), member.getId())) {
            throw new DuplicateProjectMemberException();
        }

        // ! 어떤 포지션에 지원하는지에 대한 내용이 추가되어야할 듯 싶다.
        projectMemberService.addProjectMember(findProject.getId(), member.getId(), new ProjectMemberRole(ProjectMemberRoleType.미정, ProjectMemberRoleType.미정));
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
