package fullcare.backend.oauth2;

import fullcare.backend.jwt.JwtTokenService;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.oauth2.domain.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenService jwtTokenService;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("OAuth2SuccessHandler.onAuthenticationSuccess");


        log.info("authentication = {}",authentication);
        log.info("authentication.isAuthenticated() = {}",authentication.isAuthenticated());
        log.info("authentication.getPrincipal() = {}",authentication.getPrincipal());
        log.info("authentication.getClass() = {}",authentication.getClass());



        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        log.info("token.getAuthorizedClientRegistrationId() = {}",token.getAuthorizedClientRegistrationId());



        // CustomOAuth2UserService에서 인증에 성공한 유저 정보
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        log.info("oAuth2User = {}" ,oAuth2User);

        String name = oAuth2User.getName();
        log.info("name = {}" ,name);


        //사용자에게 제공할 JWT TOKEN 생성해서 반환
        String accessToken = jwtTokenService.createAccessToken(oAuth2User);
        String refreshToken = jwtTokenService.createRefreshToken(oAuth2User);

        Member member = memberRepository.findById(Long.valueOf(name)).orElse(null);
        member.updateRefreshToken(refreshToken);
        memberRepository.save(member);

        log.info("member.refreshToken = {}",member.getRefreshToken());

        // 토큰을 돌려줄 URL 경로
        String successUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/token")
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request,response,successUrl);
    }
}