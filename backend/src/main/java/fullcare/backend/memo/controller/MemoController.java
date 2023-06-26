package fullcare.backend.memo.controller;

import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.global.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.memo.dto.request.MemoCreateRequest;
import fullcare.backend.memo.dto.request.MemoUpdateRequest;
import fullcare.backend.memo.dto.response.BookmarkMemoListResponse;
import fullcare.backend.memo.dto.response.MemoDetailResponse;
import fullcare.backend.memo.dto.response.MemoIdResponse;
import fullcare.backend.memo.dto.response.MemoListResponse;
import fullcare.backend.memo.service.BookmarkMemoService;
import fullcare.backend.memo.service.MemoService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@Tag(name = "회의록", description = "회의록 관련 API")
@RequestMapping("/api/auth/memo") // todo memo는 인증된 사용자만 접근할 수 있는 API인데, 경로를 굳이 auth를 명시해줘야할까?
@RestController
public class MemoController {

    private final MemoService memoService;
    private final ProjectMemberService projectMemberService;
    private final BookmarkMemoService bookmarkMemoService;

    // ! 공통검증요소 : API를 요청한 사용자가 회의록이 속한 프로젝트의 멤버인가?

    // * 새로운 회의록 생성
    @Operation(method = "post", summary = "회의록 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회의록 생성 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoIdResponse.class))),
            @ApiResponse(responseCode = "400", description = "회의록 생성 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity create(@RequestBody MemoCreateRequest memoCreateRequest,
                                 @CurrentLoginMember Member member) {

        if (!(projectMemberService.validateProjectMember(memoCreateRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        }

        Memo newMemo = memoService.createMemo(memoCreateRequest, member.getNickname());
        return new ResponseEntity(new MemoIdResponse(newMemo.getId()), HttpStatus.CREATED);
    }

    // * 특정 회의록 수정
    @Operation(method = "put", summary = "회의록 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의록 수정 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoIdResponse.class))),
            @ApiResponse(responseCode = "400", description = "회의록 수정 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{memoId}")
    public ResponseEntity update(@PathVariable Long memoId,
                                 @RequestBody MemoUpdateRequest memoUpdateRequest,
                                 @CurrentLoginMember Member member) {

        Memo memo = memoService.findMemo(memoId); // ! -> Project 엔티티 Fetch Join 함
        Long projectId = memo.getProject().getId();

        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        }

        memoService.updateMemo(memoId, memoUpdateRequest);
        return new ResponseEntity(new MemoIdResponse(memo.getId()), HttpStatus.OK);
    }

    // * 특정 회의록 삭제
    @Operation(method = "delete", summary = "회의록 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의록 삭제 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "회의록 삭제 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{memoId}")
    public ResponseEntity delete(@PathVariable Long memoId,
                                 @CurrentLoginMember Member member) {

        Memo memo = memoService.findMemo(memoId);
        Long projectId = memo.getProject().getId();


        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        } else if (!memo.getAuthor().equals(member.getNickname()) || !projectMemberService.findProjectMember(projectId, member.getId()).getProjectMemberRole().getRole().equals(ProjectMemberRoleType.리더)) {
            throw new InvalidAccessException("해당 회의록에 대한 삭제 권한이 없습니다.");
        }
        memoService.deleteMemo(memoId);
        return new ResponseEntity(HttpStatus.OK);
    }


    // * 특정 회의록 조회
    @Operation(method = "get", summary = "회의록 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의록 단건 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "회의록 단건 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{memoId}")
    public ResponseEntity<?> details(@PathVariable Long memoId,
                                     @CurrentLoginMember Member member) {

        Memo memo = memoService.findMemo(memoId);
        Long projectId = memo.getProject().getId();

        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        }

        MemoDetailResponse memoDetailResponse = memoService.findMemoDetailResponse(member.getId(), memoId);
        return new ResponseEntity<>(memoDetailResponse, HttpStatus.OK);
    }

    // * 회의록 목록 조회 -> 필터링 조건은 프론트에서 설정해서 날려주기
    @Operation(method = "get", summary = "회의록 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의록 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "회의록 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/list")
    public ResponseEntity<CustomPageImpl<MemoListResponse>> list(@RequestParam("project_id") Long projectId,
                                                                 @ModelAttribute CustomPageRequest pageRequest,
                                                                 @CurrentLoginMember Member member) {

        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }

        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        CustomPageImpl<MemoListResponse> responses = memoService.findMemoList(projectId, pageable);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // * 북마크한 회의록 목록 조회 -> 북마킹된 날짜를 기준으로 정렬
    @Operation(method = "get", summary = "북마크된 회의록 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "북마크 회의록 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "북마크 회의록 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/bookmarklist")
    public ResponseEntity<CustomPageImpl<BookmarkMemoListResponse>> bookmarklist(@RequestParam("project_id") Long projectId,
                                                                                 @ModelAttribute CustomPageRequest pageRequest,
                                                                                 @CurrentLoginMember Member member) {

        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }

        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        CustomPageImpl<BookmarkMemoListResponse> responses = bookmarkMemoService.findBookmarkMemoList(pageable, projectId, member.getId());

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }


    // * 특정 회의록 북마킹
    @Operation(method = "post", summary = "특정 회의록 북마킹")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의록 북마킹 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "회의록 북마킹 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{memoId}/bookmark")
    public ResponseEntity bookmark(@PathVariable Long memoId, @CurrentLoginMember Member member) {
        Memo memo = memoService.findMemo(memoId);
        Long projectId = memo.getProject().getId();

        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }

        bookmarkMemoService.bookmarkMemo(memo, member);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
