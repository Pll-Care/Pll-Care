package fullcare.backend.jwt;

import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.oauth2.domain.CustomOAuth2User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.Provider;
import java.util.Date;
import java.util.HashMap;

@Service // 따로 데이터베이스에 Token 엔티티를 저장하지 않는데 Service인가 Provider인가?
@Slf4j
public class JwtTokenService {

    private Key key;
    private final String secret;
    private final long accessTokenValidationMilliseconds;
    private final long refreshTokenValidationMilliseconds;

    private final MemberRepository memberRepository;

    @Value(("${jwt.access.header}"))
    private String accessHeader;

    @Value(("${jwt.refresh.header}"))
    private String refreshHeader;

    public JwtTokenService(@Value("${jwt.secret}") String secret, @Value("${jwt.access.token-validity-in-seconds}") long accessTokenValidationMilliseconds,
                           @Value("${jwt.refresh.token-validity-in-seconds}") long refreshTokenValidationMilliseconds, MemberRepository memberRepository) {
        this.secret = secret;
        this.accessTokenValidationMilliseconds = accessTokenValidationMilliseconds * 1000;
        this.refreshTokenValidationMilliseconds = refreshTokenValidationMilliseconds* 1000;
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    public void setJwtKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(CustomOAuth2User oAuth2User){
        // TODO:  JWT 토큰에 어떤 데이터를 넣을 것인지 결정 필요

        long now = (new Date()).getTime();
        Date validity = new Date(now + accessTokenValidationMilliseconds);

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("sub",oAuth2User.getName());
        claims.put("role",oAuth2User.getAuthorities());
        claims.put("nickname", oAuth2User.getUsername());

        return Jwts.builder()
                .setExpiration(validity)
                .addClaims(claims)
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();


    }

    public String createRefreshToken(OAuth2User oAuth2User){
        // TODO:  JWT 토큰에 어떤 데이터를 넣을 것인지 결정 필요

        long now = (new Date()).getTime();
        Date validity = new Date(now + refreshTokenValidationMilliseconds);

        return Jwts.builder()
                .setExpiration(validity)
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }


    private void reIssueAccessToken() {

    }


    public boolean validateJwtToken(String jwtToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key).build().parseClaimsJws(jwtToken);
            return true;
        }catch(SecurityException | MalformedJwtException e){
            log.info("잘못된 JWT 서명입니다.");
        }catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        }catch (UnsupportedJwtException e){
            log.info("지원되지 않는 JWT 서명입니다.");
        }catch (IllegalArgumentException e){
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        String memberId = claims.getSubject();
        Member member = memberRepository.findById(Long.valueOf(memberId)).orElse(null);
        int i = member.getOAuth2Id().indexOf('_');
        String providerName = member.getOAuth2Id().substring(0, i);

        // todo Authentication 객체 만들어서 반환

        CustomOAuth2User oAuth2User = CustomOAuth2User.create(member);
        return new OAuth2AuthenticationToken(oAuth2User, oAuth2User.getAuthorities(), providerName);
    }

}