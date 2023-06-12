package fullcare.backend.post.controller;


import fullcare.backend.global.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.post.domain.Post;
import fullcare.backend.post.dto.request.PostCreateRequest;
import fullcare.backend.post.dto.request.PostUpdateRequest;
import fullcare.backend.post.dto.response.PostDetailResponse;
import fullcare.backend.post.dto.response.PostListResponse;
import fullcare.backend.post.service.PostService;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.service.ProjectMemberService;
import fullcare.backend.security.jwt.CurrentLoginMember;
import fullcare.backend.util.CustomPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/api/auth/post")
@RestController
public class PostController {

    private final PostService postService;
    private final ProjectMemberService projectMemberService;

    // todo 구현해야하는 기능
    // * 1. 모집글 추가, 수정, 삭제 -> 모집글을 삭제할 수 있는
    // * 2. 모집글 목록 조회(페이지 + 필터링)
    // * 3. 모집글 단건조회
    // * 4. 모집글 좋아요

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

    @PatchMapping("/{postId}")
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

    @GetMapping("/{postId}")
    public ResponseEntity<?> details(@PathVariable Long postId,
                                     @CurrentLoginMember Member member) {
        PostDetailResponse postDetailResponse = postService.findPostDetailResponse(postId);
        return new ResponseEntity<>(postDetailResponse, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(@ModelAttribute CustomPageRequest pageRequest,
                                  @CurrentLoginMember Member member) { // ? @ModelAttribute가 맞는가

        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        Page<PostListResponse> responses = postService.findPostList(member.getId(), pageable);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }


    // ! like와 unlike를 합칠까말까 고민중..
    @PostMapping("/{postId}/like")
    public ResponseEntity like(@PathVariable Long postId, @CurrentLoginMember Member member) {
        postService.likePost(postId, member);

        return new ResponseEntity<>(HttpStatus.OK);

    }
}
