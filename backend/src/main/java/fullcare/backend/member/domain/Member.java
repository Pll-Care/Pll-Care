package fullcare.backend.member.domain;

import fullcare.backend.evaluation.domain.FinalTermEvaluation;
import fullcare.backend.evaluation.domain.MidtermEvaluation;
import fullcare.backend.likes.domain.Likes;
import fullcare.backend.profile.domain.Profile;
import fullcare.backend.post.domain.Post;
import fullcare.backend.projectmember.domain.ProjectMember;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate // ! 더티 체킹할 때 바뀐 필드만 변경
@Table(name = "member")
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "oauth2_id")
    private String oAuth2Id;

    @Column(name = "nickname", unique = true) // ! 닉네임 unique 처리에 대한 고민이 필요함
    private String nickname;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private MemberRole role;

    @Column(name = "signup_date")
    private LocalDateTime signupDate;

    @Column(name = "refreshToken")
    private String refreshToken;
    private String imageUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="profile_id", nullable = false)
    private Profile profile;
    //    @Column
//    private String profileContent;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Likes> likes = new HashSet<>();

    @OneToMany(mappedBy = "evaluator", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinalTermEvaluation> finalTermEvaluationsEvaluator = new ArrayList<>();

    @OneToMany(mappedBy = "evaluated", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinalTermEvaluation> finalTermEvaluationsEvaluated = new ArrayList<>();

    @OneToMany(mappedBy = "voter", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MidtermEvaluation> midtermEvaluationsVoter = new ArrayList<>();

    @OneToMany(mappedBy = "voted", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MidtermEvaluation> midtermEvaluationsVoted = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> projectMembers = new ArrayList<>();


    public Member(String oAuth2Id, String nickname, String name, String email, MemberRole role, LocalDateTime signupDate, String refreshToken, Profile profile) {
        this.oAuth2Id = oAuth2Id;
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.role = role;
        this.signupDate = signupDate;
        this.refreshToken = refreshToken;
        this.profile = profile;
    }

    public void updateOAuth2Id(String oAuth2Id) {
        this.oAuth2Id = oAuth2Id;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
