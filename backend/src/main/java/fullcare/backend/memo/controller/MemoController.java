package fullcare.backend.memo.controller;

import fullcare.backend.global.dto.FailureResponse;
import fullcare.backend.global.exception.InvalidAccessException;
import fullcare.backend.memo.dto.request.MemoCreateRequest;
import fullcare.backend.memo.dto.request.MemoDeleteRequest;
import fullcare.backend.memo.dto.request.MemoUpdateRequest;
import fullcare.backend.memo.dto.response.MemoDetailResponse;
import fullcare.backend.memo.dto.response.MemoListResponse;
import fullcare.backend.memo.service.MemoService;
import fullcare.backend.memobookmark.service.MemoBookmarkService;
import fullcare.backend.projectmember.service.ProjectMemberService;
import fullcare.backend.security.jwt.CurrentLoginUser;
import fullcare.backend.security.oauth2.domain.CustomOAuth2User;
import fullcare.backend.util.CustomPageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Getter
@RequiredArgsConstructor
@Tag(name = "회의록", description = "회의록 관련 API")
@RequestMapping("/api/auth/memo")
@RestController
public class MemoController {

    private final MemoService memoService;
    private final ProjectMemberService projectMemberService;
    private final MemoBookmarkService memoBookmarkService;

    // * 구현해야하는 기능
    // 1 .회의록 CRUD
    // 2. 회의록 목록 조회(페이지 + 필터링)
    // 3. 최근 회의록(3~5개)
    // 4. 회의록 단건조회


    // ! 공통검증요소 -> 사용자가 실제 해당 메모를 볼 수 있는 프로젝트에 속한 사용자인가?

    @Operation(method = "post", summary = "회의록 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의록 생성 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "회의록 생성 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @PostMapping
    public MemoDetailResponse create(@RequestBody MemoCreateRequest memoCreateRequest, @CurrentLoginUser CustomOAuth2User user) {
        String username = user.getUsername();

        if (!(projectMemberService.validateProjectMember(memoCreateRequest.getProjectId(), Long.parseLong(user.getName())))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }

        return memoService.createMemo(memoCreateRequest, username);
    }

    @Operation(method = "patch", summary = "회의록 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의록 수정 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "회의록 수정 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @PatchMapping
    public MemoDetailResponse update(@RequestBody MemoUpdateRequest memoUpdateRequest, @CurrentLoginUser CustomOAuth2User user) {
        String username = user.getUsername();

        return memoService.updateMemo(memoUpdateRequest, username);
    }

    @Operation(method = "delete", summary = "회의록 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의록 삭제 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "회의록 삭제 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @DeleteMapping
    public void delete(@RequestBody MemoDeleteRequest memoDeleteRequest, @CurrentLoginUser CustomOAuth2User user) {

        memoService.deleteMemo(memoDeleteRequest);

    }

    @Operation(method = "get", summary = "회의록 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의록 단건 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "회의록 단건 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/{memoId}")
    public MemoDetailResponse details(@PathVariable Long memoId, @CurrentLoginUser CustomOAuth2User user) {

        return memoService.findMemo(memoId);
    }

    @Operation(method = "get", summary = "회의록 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회의록 리스트 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoListResponse.class))),
            @ApiResponse(responseCode = "400", description = "회의록 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam("project_id") Long projectId, CustomPageRequest pageRequest, @CurrentLoginUser CustomOAuth2User user) {

        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        Page<MemoListResponse> responses = memoService.findMemoList(projectId, pageable);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping("/bookmarklist")
    public ResponseEntity bookmarklist(@RequestParam("project_id") Long projectId, CustomPageRequest pageRequest, @CurrentLoginUser CustomOAuth2User user) {
        Long memberId = Long.parseLong(user.getName());

        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        Page<MemoListResponse> responses = memoService.findBookmarkMemoList(projectId, memberId, pageable);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping("/{memoId}/bookmark")
    public ResponseEntity bookmark(@PathVariable Long memoId, @CurrentLoginUser CustomOAuth2User user) {
        Long memberId = Long.parseLong(user.getName());
        memoService.bookmarkMemo(memberId, memoId);

        return ResponseEntity.ok(HttpStatus.OK);
    }

//    @GetMapping("{projectId}/recent")
//    public ResponseEntity<?> recent(@PathVariable Long projectId, @CurrentLoginUser CustomOAuth2User user) {
//        List<MemoListResponse> responses = memoService.findRecentMemos(projectId);
//
//        return new ResponseEntity<>(responses, HttpStatus.OK);
//    }
}
