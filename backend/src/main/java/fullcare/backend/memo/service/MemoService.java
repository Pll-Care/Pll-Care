package fullcare.backend.memo.service;

import fullcare.backend.bookmarkmemo.domain.BookmarkMemo;
import fullcare.backend.bookmarkmemo.repository.BookmarkMemoRepository;
import fullcare.backend.global.errorcode.MemoErrorCode;
import fullcare.backend.global.exceptionhandling.exception.CompletedProjectException;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.global.exceptionhandling.exception.UnauthorizedAccessException;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.memo.dto.request.MemoBookmarkRequest;
import fullcare.backend.memo.dto.request.MemoCreateRequest;
import fullcare.backend.memo.dto.request.MemoDeleteRequest;
import fullcare.backend.memo.dto.request.MemoUpdateRequest;
import fullcare.backend.memo.dto.response.BookmarkMemoListResponse;
import fullcare.backend.memo.dto.response.MemoDetailResponse;
import fullcare.backend.memo.dto.response.MemoListResponse;
import fullcare.backend.memo.repository.MemoRepository;
import fullcare.backend.project.service.ProjectService;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.util.CustomPageImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    private final ProjectService projectService;

    @Transactional
    public Long createMemo(Long memberId, MemoCreateRequest request) {
        try {
            ProjectMember findProjectMember = projectService.isProjectAvailable(request.getProjectId(), memberId, false);

            Memo newMemo = Memo.createNewMemo()
                    .project(findProjectMember.getProject())
                    .title(request.getTitle())
                    .content(request.getContent())
                    .author(findProjectMember.getMember())
                    .build();

            return memoRepository.save(newMemo).getId();

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(MemoErrorCode.INVALID_CREATE);
        }
    }

    @Transactional
    public Long updateMemo(Long memberId, Long memoId, MemoUpdateRequest request) {
        try {
            projectService.isProjectAvailable(request.getProjectId(), memberId, false);

            Memo findMemo = findMemo(memoId);
            findMemo.updateAll(request.getTitle(), request.getContent());

            return findMemo.getId();

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(MemoErrorCode.INVALID_MODIFY);
        }

    }

    @Transactional
    public void deleteMemo(Long memberId, Long memoId, MemoDeleteRequest request) {

        try {
            ProjectMember findProjectMember = projectService.isProjectAvailable(request.getProjectId(), memberId, false);
            Memo findMemo = findMemo(memoId);

            if (findMemo.getAuthor().getId() != findProjectMember.getMember().getId() && !findProjectMember.isLeader()) {
                throw new UnauthorizedAccessException(MemoErrorCode.UNAUTHORIZED_DELETE);
            }

            memoRepository.delete(findMemo);

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(MemoErrorCode.INVALID_DELETE);
        }


    }

    public Memo findMemo(Long memoId) {
        return memoRepository.findById(memoId).orElseThrow(() -> new EntityNotFoundException(MemoErrorCode.MEMO_NOT_FOUND));
    }

    public MemoDetailResponse findMemoDetailResponse(Long projectId, Long memberId, Long memoId) {

        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, memberId, true);
        MemoDetailResponse findMemoDetailResponse = memoRepository.findMemoDetailDto(memberId, memoId).orElseThrow(() -> new EntityNotFoundException(MemoErrorCode.MEMO_NOT_FOUND));

        if (findMemoDetailResponse.getAuthorId() == findProjectMember.getMember().getId() || findProjectMember.isLeader()) {
            findMemoDetailResponse.setDeletable(true);
        } else {
            findMemoDetailResponse.setDeletable(false);
        }

        if (findProjectMember.getProject().isCompleted()) {
            findMemoDetailResponse.setEditable(false);
            findMemoDetailResponse.setDeletable(false);
        } else {
            findMemoDetailResponse.setEditable(true);
        }

        return findMemoDetailResponse;
    }


    public CustomPageImpl<MemoListResponse> findMemoList(Long projectId, Long memberId, Pageable pageable) {
        projectService.isProjectAvailable(projectId, memberId, true);

        Page<Memo> result = memoRepository.findMemoListByProjectId(projectId, pageable);
        List<MemoListResponse> content = result.stream().map(MemoListResponse::entityToDto)
                .collect(Collectors.toList());

        return new CustomPageImpl<>(content, pageable, result.getTotalElements());
    }

    public CustomPageImpl<BookmarkMemoListResponse> findBookmarkMemoList(Long projectId, Long memberId, Pageable pageable) {
        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, memberId, true);

        Page<BookmarkMemo> result = bookmarkMemoRepository.findList(findProjectMember.getProject().getId(), findProjectMember.getMember().getId(), pageable);
        List<BookmarkMemoListResponse> content = result.stream().map(BookmarkMemoListResponse::entityToDto).collect(Collectors.toList());

        return new CustomPageImpl<>(content, pageable, result.getTotalElements());
    }

    @Transactional
    public void bookmarkMemo(Long memberId, Long memoId, MemoBookmarkRequest request) {
        try {
            ProjectMember findProjectMember = projectService.isProjectAvailable(request.getProjectId(), memberId, false);
            Memo findMemo = findMemo(memoId);

            Optional<BookmarkMemo> findBookmarkMemo = bookmarkMemoRepository.findByMemoAndMember(findMemo, findProjectMember.getMember());

            if (!findBookmarkMemo.isPresent()) {
                BookmarkMemo newBookmarkMemo = findMemo.bookmark(findProjectMember.getMember());
                bookmarkMemoRepository.save(newBookmarkMemo);
            } else {
                bookmarkMemoRepository.delete(findBookmarkMemo.get());
            }

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(MemoErrorCode.INVALID_BOOKMARK);
        }

    }
}
