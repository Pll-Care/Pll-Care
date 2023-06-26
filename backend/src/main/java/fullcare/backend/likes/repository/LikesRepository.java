package fullcare.backend.likes.repository;

import fullcare.backend.likes.domain.Likes;
import fullcare.backend.member.domain.Member;
import fullcare.backend.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByPostAndMember(Post post, Member member);
    
}
