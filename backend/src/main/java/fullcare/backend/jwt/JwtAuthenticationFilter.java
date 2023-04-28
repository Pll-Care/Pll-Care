package fullcare.backend.jwt;

import fullcare.backend.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String ACCESS_TOKEN_AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_AUTHORIZATION_HEADER = "Authorization_refresh";

    private final JwtTokenService jwtTokenService;

    public JwtAuthenticationFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
             String refreshToken = getRefreshToken(request);

            if(refreshToken != null && jwtTokenService.validateJwtToken(refreshToken)){
                // refreshToken이 정상일 경우
//                reIssueAccessToken();
                return;
            }else{

                String accessToken = getAccessToken(request);

                if (accessToken != null && jwtTokenService.validateJwtToken(accessToken)){
                    Authentication authentication = jwtTokenService.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else {
                    log.info("No Access Token!");
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request,response);
    }

    private String getAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(ACCESS_TOKEN_AUTHORIZATION_HEADER);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(REFRESH_TOKEN_AUTHORIZATION_HEADER);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

}
