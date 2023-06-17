package fullcare.backend.memo.controller;

import fullcare.backend.global.dto.FailureResponse;
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
import fullcare.backend.projectmember.service.ProjectMemberService;
import fullcare.backend.security.jwt.CurrentLoginMember;
import fullcare.backend.util.CustomPageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    // todo 구현해야하는 API 기능
    // * 1 .회의록 추가, 수정, 삭제
    // * 2. 회의록 목록 조회(페이지 + 필터링)
    // * 3. 회의록 단건조회
    // * 4. 즐겨찾기한 회의록 리스트(페이지 + 필터링)
    // * 5. 특정 회의록 북마킹

    // ! 공통검증요소 : API를 요청한 사용자가 회의록 열람 권한이 있는 프로젝트에 속한 사용자인가?

    // ? 고민해야할 거리
    // ? @RequestBody, @RequestParam/@ModelAttribute, @PathVariable 중 어떤 걸 사용해서 HTTP 요청 데이터를 받아오는게 맞을까?

    @Operation(method = "post", summary = "회의록 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회의록 생성 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoIdResponse.class))),
            @ApiResponse(responseCode = "400", description = "회의록 생성 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
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

    @Operation(method = "patch", summary = "회의록 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의록 수정 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoIdResponse.class))),
            @ApiResponse(responseCode = "400", description = "회의록 수정 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
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

        memoService.updateMemo(memoId, memoUpdateRequest, member.getNickname());
        return new ResponseEntity(new MemoIdResponse(memo.getId()), HttpStatus.OK);
    }

    @Operation(method = "delete", summary = "회의록 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의록 삭제 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "회의록 삭제 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @DeleteMapping("/{memoId}")
    public ResponseEntity delete(@PathVariable Long memoId,
                                 @CurrentLoginMember Member member) {

        Memo memo = memoService.findMemo(memoId);
        Long projectId = memo.getProject().getId();

        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        }

        memoService.deleteMemo(memoId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(method = "get", summary = "회의록 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의록 단건 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "회의록 단건 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/{memoId}")
    public ResponseEntity<?> details(@PathVariable Long memoId,
                                     @CurrentLoginMember Member member) {

        Memo memo = memoService.findMemo(memoId);
        Long projectId = memo.getProject().getId(); // * projectId는 fk 값이라서 memo 찾을 때 갖고옴.

        // 1. 사용자가 회의록을 볼 수 있는 권한이 있는지 확인
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        }

        // 2. 사용자가 회의록에 북마크를 했는지 여부 확인
        // 3. 회의록 상세정보 전달
        MemoDetailResponse memoDetailResponse = memoService.findMemoDetailResponse(member.getId(), memoId);
        return new ResponseEntity<>(memoDetailResponse, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "회의록 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의록 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "회의록 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/list")
    public ResponseEntity<Page<MemoListResponse>> list(@RequestParam("project_id") Long projectId,
                                                       @ModelAttribute CustomPageRequest pageRequest,
                                                       @CurrentLoginMember Member member) {

        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }

        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        Page<MemoListResponse> responses = memoService.findMemoList(projectId, pageable);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "북마크된 회의록 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "북마크 회의록 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "북마크 회의록 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/bookmarklist")
    public ResponseEntity<Page<BookmarkMemoListResponse>> bookmarklist(@RequestParam("project_id") Long projectId,
                                                                       @ModelAttribute CustomPageRequest pageRequest,
                                                                       @CurrentLoginMember Member member) {

        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }

        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        Page<BookmarkMemoListResponse> responses = bookmarkMemoService.findBookmarkMemoList(pageable, projectId, member.getId());

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // ? 북마크 기능을 update 쪽으로 같이 넣어버리기?
    // todo path variable 대신 requestparam으로 넣는게 나을지도
    @Operation(method = "post", summary = "특정 회의록 북마킹")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의록 북마킹 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "회의록 북마킹 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @PostMapping("/{memoId}/bookmark")
    public ResponseEntity bookmark(@PathVariable Long memoId, @CurrentLoginMember Member member) {
        Memo memo = memoService.findMemo(memoId);
        Long projectId = memo.getProject().getId();

        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }

        bookmarkMemoService.bookmarkMemo(member, memo);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
