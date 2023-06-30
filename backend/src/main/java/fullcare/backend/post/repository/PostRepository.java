package fullcare.backend.post.repository;


import fullcare.backend.global.State;
import fullcare.backend.post.domain.Post;
import fullcare.backend.post.dto.response.PostListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select new fullcare.backend.post.dto.response.PostListResponse(p.id, pj.title, p.title, p.techStack, case when l.id is null then false else true end, p.createdDate, p.modifiedDate) " +
            "from Post p left join Likes l on l.post.id = p.id and l.member.id = :memberId " +
            "join p.project pj")
    Page<PostListResponse> findList(@Param("memberId") Long memberId, Pageable pageable);

    @Override
    @Query("select p from Post p join fetch p.author a join fetch p.project pj where p.id = :postId")
    Optional<Post> findById(@Param("postId") Long postId);

//    @Query("select new fullcare.backend.post.dto.response.PostDetailResponse(p.id, pj.title, a.nickname, p.title, p.description, p.reference, p.contact, p.region, p.techStack, case when l.id is null then false else true end, p.createdDate, p.modifiedDate)" +
//            "from Post p join p.project pj join p.author a left join Likes l on l.post.id = p.id and l.member.id = :memberId")
//    PostDetailResponse findPost(@Param("memberId") Long memberId, @Param("postId") Long postId);


    @Query(value = "select p from Post p join p.project pp join pp.projectMembers pm where pm.member.id  = :memberId and p.state = :state", countQuery = "select count(p) from Post p join p.project pp join pp.projectMembers pm where pm.member.id = :memberId and p.state = :state")
    Page<Post> findPageByMemberId(@Param("memberId") Long memberId, @Param("state") State state, Pageable pageable);

    @Query(value = "select p from Post p join fetch p.likes l where l.member.id =:memberId", countQuery = "select p from Post p join fetch p.likes l where l.member.id =:memberId")
    Page<Post> findLikePageByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
