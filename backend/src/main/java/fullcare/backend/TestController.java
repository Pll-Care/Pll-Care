package fullcare.backend;

import fullcare.backend.jwt.CurrentLoginUser;
import fullcare.backend.oauth2.domain.CustomOAuth2User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/api/test")
    public String test(@CurrentLoginUser CustomOAuth2User oAuth2User){
        log.info("oAuth2User = {}" ,oAuth2User);

        return "OK";
    }
}
