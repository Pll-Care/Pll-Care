package fullcare.backend.memobookmark.service;

import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.memo.repository.MemoRepository;
import fullcare.backend.memobookmark.domain.MemoBookmark;
import fullcare.backend.memobookmark.repository.MemoBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemoBookmarkService {

    private final MemoBookmarkRepository memoBookmarkRepository;
    private final MemberRepository memberRepository;
    private final MemoRepository memoRepository;


    public void addBookmark(Long memberId, Long memoId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Memo memo = memoRepository.findById(memoId).orElseThrow();

        MemoBookmark memoBookmark = MemoBookmark.createNewMemoBookmark()
                .member(member)
                .memo(memo)
                .build();

        memoBookmark.mark();
        memoBookmarkRepository.save(memoBookmark);
    }

    public void deleteBookmark(Long memberId, Long memoId) {
        MemoBookmark memoBookmark = memoBookmarkRepository.findByMemberIdAndMemoId(memberId, memoId).orElseThrow();
        memoBookmark.unmark();

        memoBookmarkRepository.delete(memoBookmark);
    }

    public boolean isBookmarked(Long memberId, Long memoId) {
        return memoBookmarkRepository.findByMemberIdAndMemoId(memberId, memoId).isPresent();
    }

}
