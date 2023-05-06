package fullcare.backend.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import fullcare.backend.security.jwt.exception.JwtErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
    JWT 인증 실패 경우
    1. 만료 토큰
    2. 토큰 없음
    3. 잘못된 토큰 서명
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message = (String)request.getAttribute("message");
        if(message.equals(JwtErrorCode.MALFORMED_TOKEN.getMessage())) {
            setResponse(request, response, HttpServletResponse.SC_BAD_REQUEST);
        }
        else if(message.equals(JwtErrorCode.EXPIRED_TOKEN.getMessage())) {
            setResponse(request, response, HttpServletResponse.SC_BAD_REQUEST);
        }
        else if(message.equals(JwtErrorCode.UNSUPPORTED_TOKEN.getMessage())) {
            setResponse(request, response, HttpServletResponse.SC_BAD_REQUEST);
        }
        else if(message.equals(JwtErrorCode.ILLEGAL_TOKEN.getMessage())) {
            setResponse(request, response, HttpServletResponse.SC_BAD_REQUEST);
        }
        else if(message.equals(JwtErrorCode.NOT_FOUND_USER.getMessage())) {
            setResponse(request, response, HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void setResponse(HttpServletRequest request, HttpServletResponse response, int status) throws IOException {
//        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());

        final Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", format);
        body.put("status", status);
        body.put("message", request.getAttribute("message"));
        body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
