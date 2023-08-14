package fullcare.backend.security.oauth2;

import fullcare.backend.global.errorcode.MemberErrorCode;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.security.jwt.JwtTokenService;
import fullcare.backend.security.oauth2.domain.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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

    /*
     * OAuth2 로그인이 성공하는 경우, JWT 토큰을 생성하고, 토큰 값을 URI 경로에 담에 리다이렉션 시킴.
     */

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 로그인 및 인증 성공! 이제 JWT 토큰을 생성하여 발급합니다.");
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        // CustomOAuth2UserService에서 인증에 성공한 유저 정보
        CustomOAuth2User oAuth2User = (CustomOAuth2User) token.getPrincipal();

        log.info("oAuth2User = {}", oAuth2User);
        log.info("name = {}", oAuth2User.getName());

        //사용자에게 제공할 JWT TOKEN 생성해서 반환
        String accessToken = jwtTokenService.createAccessToken(oAuth2User);
        String refreshToken = jwtTokenService.createRefreshToken(oAuth2User);

        Member member = memberRepository.findById(Long.valueOf(oAuth2User.getName())).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.updateRefreshToken(refreshToken);

        // 프론트렌드로 토큰을 돌려줄 URL 경로
        String successUrl = UriComponentsBuilder.fromUriString("https://fullcare.store/token")
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, successUrl);
    }
}