package fullcare.backend.apply.service;

import fullcare.backend.apply.domain.Apply;
import fullcare.backend.apply.repository.ApplyRepository;
import fullcare.backend.profile.dto.response.MyApplyResponse;
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
public class ApplyService {
    private final ApplyRepository applyRepository;

    public CustomPageImpl<MyApplyResponse> findMyApply(Long memberId, Pageable pageable) {
        Page<Apply> applies = applyRepository.findByMemberId(memberId, pageable);
        List<MyApplyResponse> content = applies.stream().map(a -> MyApplyResponse.builder()
                .postId(a.getPost().getId())
                .title(a.getPost().getTitle())
                .description(a.getPost().getDescription()).build()).collect(Collectors.toList());
        return new CustomPageImpl<>(content, pageable, applies.getTotalElements());
    }
}
