package fullcare.backend.memo.service;

import fullcare.backend.memo.domain.BookmarkMemo;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.memo.dto.request.MemoCreateRequest;
import fullcare.backend.memo.dto.request.MemoUpdateRequest;
import fullcare.backend.memo.dto.response.MemoDetailResponse;
import fullcare.backend.memo.dto.response.MemoListResponse;
import fullcare.backend.memo.repository.BookmarkMemoRepository;
import fullcare.backend.memo.repository.MemoRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.util.CustomPageImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final BookmarkMemoRepository bookmarkMemoRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public Memo createMemo(MemoCreateRequest request, String username) {
        Project project = projectRepository.findById(request.getProjectId()).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트가 존재하지 않습니다."));

        Memo newMemo = Memo.createNewMemo()
                .project(project)
                .title(request.getTitle())
                .content(request.getContent())
                .author(username)
                .build();

        Memo memo = memoRepository.save(newMemo);
        return memo;
    }

    @Transactional
    public void updateMemo(Long memoId, MemoUpdateRequest request, String username) {
        Memo memo = memoRepository.findById(memoId).orElseThrow(() -> new EntityNotFoundException("해당 회의록이 존재하지 않습니다."));

        memo.updateAll(request.getTitle(), request.getContent(), username);
        //        memoRepository.flush();
        //        ! JPA Auditing으로 lastModified 저장할 때, flush를 해야 반영되는지 확인이 필요

    }

    @Transactional
    public void deleteMemo(Long memoId) {
        // * 엔티티가 발견되지 않을 시, 예외 던지지 않음 (삭제가 실패되었음을 알려줄 필요는 없나?)
        memoRepository.deleteById(memoId);
    }

    public Memo findMemo(Long memoId) {
        return memoRepository.findById(memoId).orElseThrow(() -> new EntityNotFoundException("해당 회의록이 존재하지 않습니다."));
    }

    public MemoDetailResponse findMemoDetailResponse(Long memberId, Long memoId) {
        Optional<BookmarkMemo> findMemo = bookmarkMemoRepository.findByMemberIdAndMemoId(memberId, memoId);

        if (findMemo.isPresent()) {
            log.info("findMemo.isPresent() : {}", findMemo.isPresent());
            return MemoDetailResponse.entityToDto(findMemo.get().getMemo(), true);
        }

        Memo memo = memoRepository.findById(memoId).orElseThrow(() -> new EntityNotFoundException("해당 회의록이 존재하지 않습니다."));
        return MemoDetailResponse.entityToDto(memo, false);
    }


    public CustomPageImpl<MemoListResponse> findMemoList(Long projectId, Pageable pageable) {
        Page<Memo> memoList = memoRepository.findList(projectId, pageable);
        List<MemoListResponse> content = memoList.stream().map(MemoListResponse::entityToDto)
                .collect(Collectors.toList());

        return new CustomPageImpl<>(content, pageable, memoList.getTotalElements());
    }
}
