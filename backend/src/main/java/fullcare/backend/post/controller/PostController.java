package fullcare.backend.post.controller;


import fullcare.backend.global.dto.FailureResponse;
import fullcare.backend.global.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.post.domain.Post;
import fullcare.backend.post.dto.request.PostCreateRequest;
import fullcare.backend.post.dto.request.PostUpdateRequest;
import fullcare.backend.post.dto.response.PostDetailResponse;
import fullcare.backend.post.dto.response.PostListResponse;
import fullcare.backend.post.service.PostService;
import fullcare.backend.project.domain.Project;
import fullcare.backend.projectmember.domain.ProjectMember;
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
    // * 1. 모집글 생성, 수정, 삭제 -> 모집글을 삭제할 수 있는
    // * 2. 모집글 목록 조회(페이지 + 필터링)
    // * 3. 모집글 단건조회
    // * 4. 모집글 좋아요
    // * 5. 모집글을 통해 프로젝트에 지원

    // * 새로운 모집글 생성
    @Operation(method = "post", summary = "모집글 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "모집글 생성 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "모집글 생성 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @PostMapping
    public ResponseEntity create(@RequestBody PostCreateRequest postCreateRequest,
                                 @CurrentLoginMember Member member) {

        // * 해당 프로젝트의 모집글을 작성 할 수 있는 권한을 가진 사람인지 검증 필요
        // * 프로젝트에 소속된 사람인가? (프로젝트에 소속된 사람이라면 누구나 모집글을 작성할 수 있음)
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
            @ApiResponse(responseCode = "400", description = "모집글 수정 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @PutMapping("/{postId}")
    public ResponseEntity update(@PathVariable Long postId,
                                 @RequestBody PostUpdateRequest postUpdateRequest,
                                 @CurrentLoginMember Member member) {

        Post post = postService.findPost(postId); // ! 페치조인 여부 확인 필요
        ProjectMember projectMember = post.getProjectMember(); // * 작성자 정보

        // * 해당 프로젝트의 모집글을 수정 할 수 있는 권한을 가진 사람인지 검증 필요
        // * 프로젝트 리더인가? or 프로젝트 팀원이면서 작성자 본인인가?
        if (!(projectMember.getMember().getId().equals(member.getId())) && !(projectMemberService.findProjectMember(projectMember.getProject().getId(), member.getId()).isLeader())) {
            throw new InvalidAccessException("해당 모집글의 수정 권한이 없습니다.");
        }

        postService.updatePost(postId, postUpdateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }


    // * 특정 모집글 삭제
    @Operation(method = "delete", summary = "모집글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모집글 삭제 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "모집 삭제 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity delete(@PathVariable Long postId,
                                 @CurrentLoginMember Member member) {

        Post post = postService.findPost(postId); // ! 페치조인 여부 확인 필요
        ProjectMember projectMember = post.getProjectMember(); // * 작성자 정보

        // * 해당 프로젝트의 모집글을 수정 할 수 있는 권한을 가진 사람인지 검증 필요
        // * 프로젝트 리더인가? or 프로젝트 팀원이면서 작성자 본인인가?
        if (!(projectMember.getMember().getId().equals(member.getId())) && !(projectMemberService.findProjectMember(projectMember.getProject().getId(), member.getId()).isLeader())) {
            throw new InvalidAccessException("해당 모집글의 삭제 권한이 없습니다.");
        }

        postService.deletePost(postId);
        return new ResponseEntity(HttpStatus.OK);
    }


    // * 특정 모집글 조회
    // todo 좋아요 여부를 응답에 포함시켜야 하는가?
    @Operation(method = "get", summary = "모집글 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모집글 단건 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PostDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "모집글 단건 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/{postId}")
    public ResponseEntity<?> details(@PathVariable Long postId,
                                     @CurrentLoginMember Member member) {
        PostDetailResponse postDetailResponse = postService.findPostDetailResponse(postId);
        return new ResponseEntity<>(postDetailResponse, HttpStatus.OK);
    }


    // * 모집글 목록
    @Operation(method = "get", summary = "모집글 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모집글 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "모집글 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/list")
    public ResponseEntity<CustomPageImpl<PostListResponse>> list(@ModelAttribute CustomPageRequest pageRequest,
                                                                 @CurrentLoginMember Member member) { // ? @ModelAttribute가 맞는가

        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        CustomPageImpl<PostListResponse> responses = postService.findPostList(member.getId(), pageable);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }


    // * 특정 모집글 좋아요
    // ! like와 unlike를 합칠까말까 고민중..
    // todo path variable 대신 requestparam으로 넣는게 나을지도
    @Operation(method = "post", summary = "특정 모집글 좋아요")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모집글 좋아요 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "모집글 좋아요 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @PostMapping("/{postId}/like")
    public ResponseEntity like(@PathVariable Long postId, @CurrentLoginMember Member member) {
        postService.likePost(postId, member);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    // * 특정 게시글을 통해 해당 프로젝트에 지원
    // todo path variable 대신 requestparam으로 넣는게 나을지도
    @Operation(method = "post", summary = "특정 모집글을 통해 프로젝트 지원")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 지원 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 지원 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @PostMapping("/{postId}/apply")
    public ResponseEntity apply(@PathVariable Long postId, @CurrentLoginMember Member member) {
        Post post = postService.findPost(postId);
        ProjectMember projectMember = post.getProjectMember();
        Project project = projectMember.getProject();

        // ! 이미 프로젝트에 소속된 사람이라면 처리할 필요 없음
        if (projectMemberService.validateProjectMember(project.getId(), member.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        projectMemberService.addProjectMember(project.getId(), member.getId(), new ProjectMemberRole(ProjectMemberRoleType.미정, ProjectMemberRoleType.미정));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
