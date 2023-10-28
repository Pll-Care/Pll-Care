package fullcare.backend.likes.repository;

import fullcare.backend.likes.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByPostIdAndMemberId(Long postId, Long memberId);

}
