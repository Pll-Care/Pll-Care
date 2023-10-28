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

import static fullcare.backend.security.jwt.exception.JwtErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String ACCESS_TOKEN_AUTHORIZATION_HEADER = "Authorization";

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

    private void setRequestAttribute(HttpServletRequest request, String message) {
        if (message.equals(MALFORMED_TOKEN.getMessage())) {
            request.setAttribute("message", MALFORMED_TOKEN.getMessage());
        }else if (message.equals(EXPIRED_TOKEN.getMessage())) {
            request.setAttribute("message", EXPIRED_TOKEN.getMessage());
        }else if (message.equals(UNSUPPORTED_TOKEN.getMessage())) {
            request.setAttribute("message", UNSUPPORTED_TOKEN.getMessage());
        }else if (message.equals(ILLEGAL_TOKEN.getMessage())) {
            request.setAttribute("message", ILLEGAL_TOKEN.getMessage());
        }else if (message.equals(NOT_FOUND_USER.getMessage())) {
            request.setAttribute("message", NOT_FOUND_USER.getMessage());
        }else if (message.equals(NOT_FOUND_TOKEN.getMessage())) {
            request.setAttribute("message", NOT_FOUND_TOKEN.getMessage());
        }
    }

    private String getAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(ACCESS_TOKEN_AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}
