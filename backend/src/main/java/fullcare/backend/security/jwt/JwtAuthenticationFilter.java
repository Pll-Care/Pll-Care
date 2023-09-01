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
        try {
            String accessToken = getAccessToken(request);

            if (accessToken != null && jwtTokenService.validateJwtToken(accessToken)) {
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
    
}
