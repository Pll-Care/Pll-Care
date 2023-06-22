package fullcare.backend.post.repository;


import fullcare.backend.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p")
    Page<Post> findList(Pageable pageable);

    @Query("select p " +
            "from Post p join fetch p.recruitments left join Likes l on l.post.id = p.id and l.member.id = :memberId")
    List<Post> findList(@Param("memberId") Long memberId);

}
