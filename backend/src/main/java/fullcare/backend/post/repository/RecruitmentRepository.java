package fullcare.backend.post.repository;

import fullcare.backend.post.domain.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    @Query("select r from Recruitment r where r.post.id in :postIds")
    List<Recruitment> findByPostIds(@Param("postIds") List<Long> postIds);
    
    List<Recruitment> findByPostId(Long postId);

}
