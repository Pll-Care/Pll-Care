package fullcare.backend.likes;

import fullcare.backend.member.domain.Member;
import fullcare.backend.post.domain.Post;
import jakarta.persistence.*;

@Entity
public class Likes {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


}
