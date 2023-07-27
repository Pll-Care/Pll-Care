package fullcare.backend.memo.service;

import fullcare.backend.global.errorcode.MemoErrorCode;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.memo.dto.request.MemoCreateRequest;
import fullcare.backend.memo.dto.request.MemoUpdateRequest;
import fullcare.backend.memo.dto.response.MemoDetailResponse;
import fullcare.backend.memo.dto.response.MemoListResponse;
import fullcare.backend.memo.repository.MemoRepository;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.util.CustomPageImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemoService {

    private final MemoRepository memoRepository;

    @Transactional
    public Memo createMemo(ProjectMember projectMember, MemoCreateRequest request) {
        Memo newMemo = Memo.createNewMemo()
                .project(projectMember.getProject())
                .title(request.getTitle())
                .content(request.getContent())
                .author(projectMember.getMember())
                .build();

        return memoRepository.save(newMemo);
    }

    @Transactional
    public void updateMemo(Long memoId, MemoUpdateRequest request) {
        Memo findMemo = findMemo(memoId);
        findMemo.updateAll(request.getTitle(), request.getContent());
    }

    @Transactional
    public void deleteMemo(Memo memo) {
        memoRepository.delete(memo);
    }

    public Memo findMemo(Long memoId) {
        return memoRepository.findById(memoId).orElseThrow(() -> new EntityNotFoundException(MemoErrorCode.MEMO_NOT_FOUND));
    }

    public MemoDetailResponse findMemoDetailResponse(Long memberId, Long memoId) {
        return memoRepository.findMemoDetailDto(memberId, memoId).orElseThrow(() -> new EntityNotFoundException(MemoErrorCode.MEMO_NOT_FOUND));
    }


    public CustomPageImpl<MemoListResponse> findMemoList(Long projectId, Pageable pageable) {
        Page<Memo> result = memoRepository.findMemoListByProjectId(projectId, pageable);
        List<MemoListResponse> content = result.stream().map(MemoListResponse::entityToDto)
                .collect(Collectors.toList());

        return new CustomPageImpl<>(content, pageable, result.getTotalElements());
    }
}
