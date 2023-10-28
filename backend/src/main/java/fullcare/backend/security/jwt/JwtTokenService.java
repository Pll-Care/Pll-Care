package fullcare.backend.security.jwt;


import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.security.jwt.exception.CustomJwtException;
import fullcare.backend.security.jwt.exception.JwtErrorCode;
import fullcare.backend.security.oauth2.domain.CustomOAuth2User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Service
@Slf4j
public class JwtTokenService {

    private final String secret;
    private final long accessTokenValidationMilliseconds;
    private final long refreshTokenValidationMilliseconds;
    private final MemberRepository memberRepository;

    private Key key;

    public JwtTokenService(@Value("${jwt.secret}") String secret, @Value("${jwt.access.token-validity-in-seconds}") long accessTokenValidationMilliseconds,
                           @Value("${jwt.refresh.token-validity-in-seconds}") long refreshTokenValidationMilliseconds, MemberRepository memberRepository) {
        this.secret = secret;
        this.accessTokenValidationMilliseconds = accessTokenValidationMilliseconds * 1000;
        this.refreshTokenValidationMilliseconds = refreshTokenValidationMilliseconds * 1000;
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    public void setJwtKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(CustomOAuth2User oAuth2User) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenValidationMilliseconds);

        return Jwts.builder()
                .setSubject(oAuth2User.getName())
                .claim("role", oAuth2User.getAuthorities())
                .claim("username", oAuth2User.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .compact();
    }


    public String createRefreshToken(CustomOAuth2User oAuth2User) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenValidationMilliseconds);

        return Jwts.builder()
                .setSubject(oAuth2User.getName())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .compact();
    }


    @Transactional
    public String[] reIssueTokens(String refreshToken) {
        Member findMember = getMemberByJwtToken(refreshToken);
        String[] tokens = new String[2];

        if (findMember.getRefreshToken().equals(refreshToken)) {
            CustomOAuth2User findUser = CustomOAuth2User.create(findMember);
            String newAccessToken = createAccessToken(findUser);
            String newRefreshToken = createRefreshToken(findUser);

            tokens[0] = newAccessToken;
            tokens[1] = newRefreshToken;
            findMember.updateRefreshToken(newRefreshToken);
            return tokens;
        }

        log.info("등록되지 않은 사용자입니다.");

        // ! RTR에 의해 이미 교체된 토큰일 경우 -> 즉, 이전에 한번 사용된 리프레시 토큰일 경우
        throw new CustomJwtException(JwtErrorCode.INVALID_REFRESH_TOKEN);
    }


    public boolean validateJwtToken(String jwtToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key).build().parseClaimsJws(jwtToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new CustomJwtException(JwtErrorCode.MALFORMED_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw new CustomJwtException(JwtErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 서명입니다.");
            throw new CustomJwtException(JwtErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new CustomJwtException(JwtErrorCode.ILLEGAL_TOKEN);
        }
    }

    private Member getMemberByJwtToken(String jwtToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();

        String memberId = claims.getSubject();
        return memberRepository.findById(Long.valueOf(memberId)).orElseThrow(() -> new CustomJwtException(JwtErrorCode.NOT_FOUND_USER));
    }


    public Authentication getAuthentication(String jwtToken) {
        Member findMember = getMemberByJwtToken(jwtToken);

        return new UsernamePasswordAuthenticationToken(findMember, null, Collections.singletonList(new SimpleGrantedAuthority(findMember.getRole().getValue())));
    }

}
