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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
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
//        ConcurrentHashMap<String, Object> claims = new ConcurrentHashMap<>();
//        HashMap<String, Object> claims = new HashMap<>();
//        claims.put("sub", oAuth2User.getName());
//        claims.put("role", oAuth2User.getAuthorities());
//        claims.put("nickname", oAuth2User.getUsername());

        return Jwts.builder()
                .setSubject(oAuth2User.getName())
                .claim("role", oAuth2User.getAuthorities())
                .claim("nickname", oAuth2User.getMember().getNickname())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidationMilliseconds))
                .signWith(key, SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    public String createRefreshToken(CustomOAuth2User oAuth2User) {

        return Jwts.builder()
                .setSubject(oAuth2User.getName())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidationMilliseconds))
                .signWith(key, SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .compact();
    }


    @Transactional
    public String[] reIssueTokens(String refreshToken) {
        Authentication authentication = getAuthentication(refreshToken);
        Member findMember = (Member) authentication.getPrincipal();

        String[] tokens = new String[2];

        if (findMember.getRefreshToken().equals(refreshToken)) {
            CustomOAuth2User findUser = CustomOAuth2User.create(findMember);
            String newRefreshToken = createRefreshToken(findUser);
            String newAccessToken = createAccessToken(findUser);
            tokens[0] = newAccessToken;
            tokens[1] = newRefreshToken;
            findMember.updateRefreshToken(newRefreshToken);
            return tokens;
        }

        log.info("등록되지 않은 사용자입니다.");
        throw new CustomJwtException(JwtErrorCode.NOT_FOUND_USER);
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

    public Authentication getAuthentication(String jwtToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();

        String memberId = claims.getSubject();

        Member member = memberRepository.findById(Long.valueOf(memberId)).orElseThrow(() -> new CustomJwtException(JwtErrorCode.NOT_FOUND_USER));
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(member.getRole().getValue()));

        return new UsernamePasswordAuthenticationToken(member, null, authorities);
    }

}
