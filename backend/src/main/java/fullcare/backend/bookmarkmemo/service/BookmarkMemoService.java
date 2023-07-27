package fullcare.backend.bookmarkmemo.service;

import fullcare.backend.bookmarkmemo.domain.BookmarkMemo;
import fullcare.backend.bookmarkmemo.repository.BookmarkMemoRepository;
import fullcare.backend.member.domain.Member;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.memo.dto.response.BookmarkMemoListResponse;
import fullcare.backend.util.CustomPageImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    private final BookmarkMemoRepository bookmarkMemoRepository;

    public CustomPageImpl<BookmarkMemoListResponse> findBookmarkMemoList(Pageable pageable, Long projectId, Long memberId) {
        Page<BookmarkMemo> result = bookmarkMemoRepository.findList(pageable, projectId, memberId);
        List<BookmarkMemoListResponse> content = result.stream().map(BookmarkMemoListResponse::entityToDto).collect(Collectors.toList());

        return new CustomPageImpl<>(content, pageable, result.getTotalElements());
    }

    @Transactional
    public void bookmarkMemo(Memo memo, Member member) {
        Optional<BookmarkMemo> findBookmarkMemo = bookmarkMemoRepository.findByMemoAndMember(memo, member);

        if (!findBookmarkMemo.isPresent()) {
            BookmarkMemo newBookmarkMemo = BookmarkMemo.createNewBookmarkMemo()
                    .member(member)
                    .memo(memo)
                    .build();

            bookmarkMemoRepository.save(newBookmarkMemo);
        } else {
            bookmarkMemoRepository.delete(findBookmarkMemo.get());
        }
    }
}