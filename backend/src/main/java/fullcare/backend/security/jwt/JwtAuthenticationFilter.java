package fullcare.backend.security.jwt;

import fullcare.backend.security.jwt.exception.CustomJwtException;
import fullcare.backend.security.jwt.exception.JwtErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String ACCESS_TOKEN_AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_AUTHORIZATION_HEADER = "Authorization_refresh";

    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // todo 토큰이 없어도 모두가 접근이 가능한 API와 토큰을 이용해 인증을 해야만 접근 가능한 API를 어떻게 구분해야하는가?
        try {
            String refreshToken = getRefreshToken(request);
            String accessToken = getAccessToken(request);

            if (refreshToken != null && jwtTokenService.validateJwtToken(refreshToken)) {

                String[] newTokens = jwtTokenService.reIssueTokens(refreshToken); // * 리프레쉬 토큰이 DB와 일치 시 access, refresh 재발급
                String requestURI = request.getRequestURI();
                String successUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/token")
                        .queryParam("access_token", newTokens[0])
                        .queryParam("refresh_token", newTokens[1])
                        .queryParam("redirect_uri", requestURI)
                        .build().toUriString();
                System.out.println("successUrl = " + successUrl);


//                response.setHeader(ACCESS_TOKEN_AUTHORIZATION_HEADER, newTokens[0]);
//                response.setHeader(REFRESH_TOKEN_AUTHORIZATION_HEADER, newTokens[1]);
//                response.setStatus(200);


                response.sendRedirect(successUrl);

                return;
            } else if (accessToken != null && jwtTokenService.validateJwtToken(accessToken)) {
                Authentication authentication = jwtTokenService.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else { // ! accessToken 값 자체가 제공 안된 경우, AnonymousAuthenticationToken 으로 AuthorizationFilter에서 걸러짐
                request.setAttribute("error_code", JwtErrorCode.TOKEN_NOT_FOUND);
            }
        } catch (CustomJwtException e) {
            request.setAttribute("error_code", e.getErrorCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(ACCESS_TOKEN_AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    private String getRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(REFRESH_TOKEN_AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
