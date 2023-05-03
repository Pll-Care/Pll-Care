package fullcare.backend.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

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
            String accessToken = getAccessToken(request);
            if(refreshToken != null && jwtTokenService.validateJwtToken(refreshToken)){

                Authentication authentication = jwtTokenService.getAuthentication(refreshToken);
                String[] newTokens = jwtTokenService.reIssueTokens(refreshToken, authentication); // 리프레쉬 토큰이 DB와 일치 시 access, refresh 재발급

                String successUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/token")
                        .queryParam("access_token", newTokens[0])
                        .queryParam("refresh_token", newTokens[1])
                        .build().toUriString();
                System.out.println("successUrl = " + successUrl);

                // todo 이건 왜 필요한가?
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // ? 토큰이 성공적으로 재발급되었을 때, 바로 이어서 API 접근을 가능하게 할 것 인지, 리다이렉션 시킬 것인지 고민이 필요함
                response.sendRedirect(successUrl);
                return;
            }else{


                if (accessToken != null && jwtTokenService.validateJwtToken(accessToken)){
                    Authentication authentication = jwtTokenService.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else {
                    log.info("No Access Token!");

                    // todo 리다이렉션 시키는게 맞나?
                    //response.sendError(400,"로그인이 필요합니다.");
                    response.sendRedirect("http://localhost:3000");
                    return;

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
