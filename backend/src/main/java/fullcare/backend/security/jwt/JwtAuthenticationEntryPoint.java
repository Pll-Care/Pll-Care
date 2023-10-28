package fullcare.backend.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.security.jwt.exception.JwtErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static fullcare.backend.security.jwt.exception.JwtErrorCode.*;

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
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        JwtErrorCode errorCode = (JwtErrorCode) request.getAttribute("error_code");
        setResponse(request, response, errorCode);
    }

    private void setResponse(HttpServletRequest request, HttpServletResponse response, JwtErrorCode errorCode) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.getResponse(errorCode, request);

        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorCode.getStatus().value());
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
