package fullcare.backend.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.global.errorcode.GlobalErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    /*
     ? OAuth2 로그인이 실패하는 경우, 어떻게 처리할 것인가? -> 비정상적인 루트로 로그인을 시도하려고 하는 경우, 들어온 요청을 거부하고, localhost:3000/ 로 리다이렉션?
     */

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("OAuth2FailureHandler.onAuthenticationFailure");
        log.info("OAuth2 Authentication Fail! :", exception);

        ErrorResponse errorResponse = ErrorResponse.getResponse(GlobalErrorCode.OAUTH2_AUTHENTICATION_FAIL, exception, request);

        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(GlobalErrorCode.OAUTH2_AUTHENTICATION_FAIL.getStatus().value());
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
