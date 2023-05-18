package fullcare.backend.memo.service;

import fullcare.backend.member.domain.Member;
import fullcare.backend.memo.domain.BookmarkMemo;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.memo.dto.response.BookmarkMemoListResponse;
import fullcare.backend.memo.repository.BookmarkMemoRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookmarkMemoService {

    private final EntityManagerFactory emf;
    private final BookmarkMemoRepository bookmarkMemoRepository;

    public Page<BookmarkMemoListResponse> findBookmarkMemoList(Pageable pageable, Long projectId, Long memberId) {
        List<BookmarkMemo> bookmarkMemoList = bookmarkMemoRepository.findList(projectId, memberId);

        List<BookmarkMemoListResponse> content = bookmarkMemoList.stream().map(BookmarkMemoListResponse::entityToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, content.size());
    }

    @Transactional
    // ! 여기서 Member와 Memo에 대해서 조회 쿼리를 한번 더 날리는가? -> 추가 조회를 날리진 않음
    public void bookmarkMemo(Member member, Memo memo) {
        Optional<BookmarkMemo> findBookmarkMemo = bookmarkMemoRepository.findByMemberAndMemo(member, memo);

        // todo 여기서 member와 memo를 다시 조회해서 영속성 컨텍스트에 올려야하는가?

        if (!(findBookmarkMemo.isPresent())) {
            BookmarkMemo newBookmarkMemo = BookmarkMemo.createNewBookmarkMemo()
                    .member(member)
                    .memo(memo)
                    .build();

            newBookmarkMemo.mark();
            bookmarkMemoRepository.save(newBookmarkMemo);
        } else {
            BookmarkMemo bookmarkMemo = findBookmarkMemo.get();
            bookmarkMemo.unmark();
            bookmarkMemoRepository.delete(bookmarkMemo);
        }
    }
}
