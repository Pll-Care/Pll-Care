package fullcare.backend.memo.service;

import fullcare.backend.member.domain.Member;
import fullcare.backend.memo.domain.BookmarkMemo;
import fullcare.backend.memo.domain.Memo;
import fullcare.backend.memo.dto.response.BookmarkMemoListResponse;
import fullcare.backend.memo.repository.BookmarkMemoRepository;
import fullcare.backend.util.CustomPageImpl;
import jakarta.persistence.EntityManagerFactory;
import fullcare.backend.util.CustomPageImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookmarkMemoService {

    private final BookmarkMemoRepository bookmarkMemoRepository;


    public CustomPageImpl<BookmarkMemoListResponse> findBookmarkMemoList(Pageable pageable, Long projectId, Long memberId) {

        Page<BookmarkMemo> result = bookmarkMemoRepository.findList(pageable, projectId, memberId);
        result.stream().map(BookmarkMemoListResponse::entityToDto).collect(Collectors.toList());

        // Page<BookmarkMemoListResponse> result2 = bookmarkMemoRepository.findDtoList(pageable, projectId, memberId);

        return new CustomPageImpl<>(result.getContent(), pageable, result.getTotalElements());
    }

    @Transactional
    public void bookmarkMemo(Memo memo, Member member) {
        Optional<BookmarkMemo> findBookmarkMemo = bookmarkMemoRepository.findByMemoAndMember(memo, member);

        if (!(findBookmarkMemo.isPresent())) {
            BookmarkMemo newBookmarkMemo = BookmarkMemo.createNewBookmarkMemo()
                    .member(member)
                    .memo(memo)
                    .build();

            bookmarkMemoRepository.save(newBookmarkMemo);
        } else {
            BookmarkMemo bookmarkMemo = findBookmarkMemo.get();
            bookmarkMemoRepository.delete(bookmarkMemo);
        }
    }
}
