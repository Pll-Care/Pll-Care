package fullcare.backend.memo.controller;

import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.global.errorcode.MemoErrorCode;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.InvalidAccessException;
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
import fullcare.backend.projectmember.domain.ProjectMember;
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

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Tag(name = "회의록", description = "회의록 관련 API")
@RequestMapping("/api/auth/memo")
@RestController
public class MemoController {

    private final MemoService memoService;
    private final ProjectMemberService projectMemberService;
    private final BookmarkMemoService bookmarkMemoService;

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
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
        }

        Memo newMemo = memoService.createMemo(memoCreateRequest, member);
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

        Memo memo = memoService.findMemo(memoId);
        Long projectId = memo.getProject().getId();

        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
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
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
        } else if (memo.getAuthor().getId() != member.getId() || projectMemberService.findProjectMember(projectId, member.getId()).getProjectMemberRole().getRole() != ProjectMemberRoleType.리더) {
            throw new InvalidAccessException(MemoErrorCode.INVALID_DELETE);
        }

        // ? 회의록 있는지 없는지 확인 -> 프로젝트 소속 확인 : 이 순서대로 해야한다는게 이상함

        // ! 프로젝트 소속 확인 -> 회의록 있는지 없는지 확인

        // ! 프로젝트 소속 확인 -> 일정 있는지 없는지 확인

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

        Optional<ProjectMember> projectMember = projectMemberService.findProjectMemberOptional(projectId, member.getId());

        if (!projectMember.isPresent()) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
        }

        MemoDetailResponse memoDetailResponse = memoService.findMemoDetailResponse(member.getId(), memoId);
        memoDetailResponse.setEditable(true);

        if (memo.getAuthor().getId() == member.getId() || projectMember.get().isLeader()) {
            memoDetailResponse.setDeletable(true);
        } else {
            memoDetailResponse.setDeletable(false);
        }

        return new ResponseEntity<>(memoDetailResponse, HttpStatus.OK);
    }

    // * 회의록 목록 조회
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
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
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
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
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
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS);
        }

        bookmarkMemoService.bookmarkMemo(memo, member);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
