package fullcare.backend.post.repository;


import fullcare.backend.global.State;
import fullcare.backend.post.domain.Post;
import fullcare.backend.post.dto.response.PostDetailResponse;
import fullcare.backend.post.dto.response.PostListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select new fullcare.backend.post.dto.response.PostListResponse(p.id, pj.title, pj.imageUrl, p.title,p.recruitStartDate,p.recruitEndDate, p.techStack, case when l.id is null then false else true end, p.createdDate, p.modifiedDate) " +
            "from Post p left join Likes l on l.post.id = p.id and l.member.id = :memberId " +
            "join p.project pj")
    Page<PostListResponse> findList(@Param("memberId") Long memberId, Pageable pageable);

    @EntityGraph(attributePaths = {"project"})
    Optional<Post> findPostWithProjectById(Long postId);

    @EntityGraph(attributePaths = {"recruitments", "project"})
    Optional<Post> findPostWithRecruitmentsAndProjectById(Long postId);


    @Query("select new fullcare.backend.post.dto.response.PostDetailResponse(p.id, pj.id, pj.title,pj.imageUrl, a.nickname,a.imageUrl, p.title, p.description,p.recruitStartDate, p.recruitEndDate, p.reference, p.contact, p.region, p.techStack, case when l.id is null then false else true end, case when a.id = :memberId then true else false end , case when a.id = :memberId then true else false end ,p.createdDate, p.modifiedDate)" +
            "from Post p left join Likes l on l.post.id = p.id and l.member.id = :memberId " +
            "join p.project pj join p.author a where p.id = :postId")
    Optional<PostDetailResponse> findPostDto(@Param("memberId") Long memberId, @Param("postId") Long postId);


    @Query(value = "select p from Post p join p.project pp where p.author.id  = :memberId and p.state = :state",
            countQuery = "select count(p) from Post p join p.project pp where p.author.id = :memberId and p.state = :state")
    Page<Post> findPageByMemberId(@Param("memberId") Long memberId, @Param("state") State state, Pageable pageable);

    @Query(value = "select p from Post p join fetch p.likes l where l.member.id =:memberId",
            countQuery = "select p from Post p join fetch p.likes l where l.member.id =:memberId")
    Page<Post> findLikePageByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @EntityGraph(attributePaths = {"project"})
    List<Post> findTop5ByRecruitEndDateAfterOrderByLikeCountDescRecruitEndDateAsc(LocalDate baseDate);

    @EntityGraph(attributePaths = {"project"})
    List<Post> findTop5ByRecruitEndDateAfterOrderByRecruitEndDateAsc(LocalDate baseDate);

    @EntityGraph(attributePaths = {"project"})
    List<Post> findTop5ByCreatedDateAfterOrderByCreatedDateAscRecruitEndDateAsc(LocalDate baseDate);

}
