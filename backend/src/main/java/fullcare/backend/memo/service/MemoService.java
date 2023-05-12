package fullcare.backend.memo.service;

import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.memo.dto.request.MemoCreateRequest;
import fullcare.backend.memo.dto.request.MemoDeleteRequest;
import fullcare.backend.memo.dto.request.MemoUpdateRequest;
import fullcare.backend.memo.dto.response.MemoDetailResponse;
import fullcare.backend.memo.dto.response.MemoListResponse;
import fullcare.backend.memo.repository.MemoRepository;
import fullcare.backend.memobookmark.domain.MemoBookmark;
import fullcare.backend.memobookmark.repository.MemoBookmarkRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final MemberRepository memberRepository;
    private final MemoBookmarkRepository memoBookmarkRepository;
    private final ProjectRepository projectRepository;


    public MemoDetailResponse createMemo(MemoCreateRequest request, String username) {
        Project project = projectRepository.findById(request.getProjectId()).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트가 존재하지 않습니다."));
        Memo newMemo = Memo.createNewMemo()
                .project(project)
                .title(request.getTitle())
                .content(request.getContent())
                .author(username)
                .build();

        memoRepository.save(newMemo);

        // ? flush 해야하나?

        return MemoDetailResponse.entityToDto(newMemo);
    }

    @Transactional
    public MemoDetailResponse updateMemo(MemoUpdateRequest request, String username) {
        Memo memo = memoRepository.findById(request.getMemoId()).orElseThrow(() -> new EntityNotFoundException("해당 회의록이 존재하지 않습니다."));
        memo.updateAll(request.getTitle(), request.getContent(), username);

        //        memoRepository.flush();
        //        ! JPA Auditing으로 lastModified 저장할 때, flush를 해야 반영되는지 확인이 필요

        return MemoDetailResponse.entityToDto(memo);

    }

    @Transactional
    public void deleteMemo(MemoDeleteRequest request) {

        // * 엔티티가 발견되지 않을 시, 예외 던지지 않음
        memoRepository.deleteById(request.getMemoId());
    }

    public MemoDetailResponse findMemo(Long memoId) {
        Memo memo = memoRepository.findById(memoId).orElseThrow(() -> new EntityNotFoundException("해당 회의록이 존재하지 않습니다."));
        return MemoDetailResponse.entityToDto(memo);

    }

    public Page<MemoListResponse> findMemoList(Long projectId, Pageable pageable) {
        Page<Memo> memoList = memoRepository.findList(pageable, projectId);
        List<MemoListResponse> content = memoList.stream().map(MemoListResponse::entityToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, content.size());
    }

    public Page<MemoListResponse> findBookmarkMemoList(Long projectId, Long memberId, Pageable pageable) {
        Page<Memo> bookmarkList = memoRepository.findBookmarkList(pageable, projectId, memberId);
        List<MemoListResponse> content = bookmarkList.stream().map(MemoListResponse::entityToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, content.size());
    }

    public void bookmarkMemo(Long memberId, Long memoId) {
        // 페치조인으로 memobookmark 갖고올 때 memo까지 갖고와서 memo만 뽑아서 Dto로 바꾸고, 조작하면 될거같다.
        Memo memo = memoRepository.findById(memoId).orElseThrow();
        Member member = memberRepository.findById(memberId).orElseThrow();

        MemoBookmark memoBookmark = memoBookmarkRepository.findByMemberAndMemo(member, memo).orElse(null);

        if (memoBookmark == null) {
            MemoBookmark newMemoBookmark = MemoBookmark.createNewMemoBookmark()
                    .member(member)
                    .memo(memo)
                    .build();

            newMemoBookmark.mark();
        } else {
            memoBookmark.unmark();
            memoBookmarkRepository.delete(memoBookmark);
        }

    }
}
