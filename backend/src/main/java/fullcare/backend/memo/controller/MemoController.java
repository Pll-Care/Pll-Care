package fullcare.backend.memo.controller;

import fullcare.backend.global.exception.InvalidAccessException;
import fullcare.backend.memo.dto.request.MemoCreateRequest;
import fullcare.backend.memo.dto.request.MemoDeleteRequest;
import fullcare.backend.memo.dto.request.MemoUpdateRequest;
import fullcare.backend.memo.dto.response.MemoDetailResponse;
import fullcare.backend.memo.dto.response.MemoListResponse;
import fullcare.backend.memo.service.MemoService;
import fullcare.backend.projectmember.service.ProjectMemberService;
import fullcare.backend.security.jwt.CurrentLoginUser;
import fullcare.backend.security.oauth2.domain.CustomOAuth2User;
import fullcare.backend.util.CustomPageRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Getter
@RequiredArgsConstructor
@RequestMapping("/api/auth/memo")
@RestController
public class MemoController {

    private final MemoService memoService;
    private final ProjectMemberService projectMemberService;

    // * 구현해야하는 기능
    // 1 .회의록 CRUD
    // 2. 회의록 목록 조회(페이지 + 필터링)
    // 3. 최근 회의록(3~5개)
    // 4. 회의록 단건조회


    // ! 공통검증요소 -> 사용자가 실제 해당 메모를 볼 수 있는 프로젝트에 속한 사용자인가?
    @PostMapping
    public MemoDetailResponse create(@RequestBody MemoCreateRequest memoCreateRequest, @CurrentLoginUser CustomOAuth2User user) {

        if (!(projectMemberService.validateProjectMember(memoCreateRequest.getProjectId(), Long.parseLong(user.getName())))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }

        return memoService.createMemo(memoCreateRequest);
    }

    @PatchMapping
    public MemoDetailResponse update(@RequestBody MemoUpdateRequest memoUpdateRequest, @CurrentLoginUser CustomOAuth2User user) {

        return memoService.updateMemo(memoUpdateRequest);
    }

    @DeleteMapping
    public void delete(@RequestBody MemoDeleteRequest memoDeleteRequest, @CurrentLoginUser CustomOAuth2User user) {

        memoService.deleteMemo(memoDeleteRequest);

    }

    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam Long projectId, CustomPageRequest pageRequest, @CurrentLoginUser CustomOAuth2User user) {

        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        Page<MemoListResponse> responses = memoService.findMemoList(projectId, pageable);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }


    @GetMapping("/{memoId}")
    public MemoDetailResponse details(@PathVariable Long memoId, @CurrentLoginUser CustomOAuth2User user) {

        return memoService.findMemo(memoId);
    }

//    @GetMapping("{projectId}/recent")
//    public ResponseEntity<?> recent(@PathVariable Long projectId, @CurrentLoginUser CustomOAuth2User user) {
//        List<MemoListResponse> responses = memoService.findRecentMemos(projectId);
//
//        return new ResponseEntity<>(responses, HttpStatus.OK);
//    }
}
