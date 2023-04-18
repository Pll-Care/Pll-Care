package fullcare.backend.member.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "oauth2_id")
    private String oAuth2Id;

    @Column(name = "member_nickname")
    private String nickname;

    @Column(name = "member_name")
    private String name;

    @Column(name = "member_email")
    private String email;

    @Column(name = "member_authority")
    private String authority;

    @Column(name = "member_signup_date")
    private LocalDateTime signupDate;

    @Column(name = "member_refreshToken")
    private String refreshToken;

//    @Column
//    private String profileContent;
}
