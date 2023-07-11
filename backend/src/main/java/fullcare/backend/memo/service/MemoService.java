package fullcare.backend.memo.service;

import fullcare.backend.global.errorcode.MemoErrorCode;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.memo.dto.request.MemoCreateRequest;
import fullcare.backend.memo.dto.request.MemoUpdateRequest;
import fullcare.backend.memo.dto.response.MemoDetailResponse;
import fullcare.backend.memo.dto.response.MemoListResponse;
import fullcare.backend.memo.repository.MemoRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.repository.ProjectRepository;
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
    private final ProjectRepository projectRepository;

    @Transactional
    public Memo createMemo(MemoCreateRequest request, Member author) {
        Project project = projectRepository.findById(request.getProjectId()).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));

        Memo newMemo = Memo.createNewMemo()
                .project(project)
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .build();

        return memoRepository.save(newMemo);
    }

    @Transactional
    public void updateMemo(Long memoId, MemoUpdateRequest request) {
        Memo findMemo = findMemo(memoId);
        findMemo.updateAll(request.getTitle(), request.getContent());
    }

    @Transactional
    public void deleteMemo(Long memoId) {
        // TODO check 필요. 엔티티가 발견되지 않을 시, 예외 던지지 않음 (삭제가 실패되었음을 알려줄 필요는 없나?)
        memoRepository.deleteById(memoId);
    }

    public Memo findMemo(Long memoId) {
        return memoRepository.findById(memoId).orElseThrow(() -> new EntityNotFoundException(MemoErrorCode.MEMO_NOT_FOUND));
    }

    public MemoDetailResponse findMemoDetailResponse(Long memberId, Long memoId) {
        MemoDetailResponse result = memoRepository.findMemoDetailDto(memberId, memoId);
        return result;
    }


    public CustomPageImpl<MemoListResponse> findMemoList(Long projectId, Pageable pageable) {
        Page<Memo> result = memoRepository.findMemoListByProjectId(projectId, pageable);
        List<MemoListResponse> content = result.stream().map(MemoListResponse::entityToDto)
                .collect(Collectors.toList());

        return new CustomPageImpl<>(content, pageable, result.getTotalElements());
    }
}
