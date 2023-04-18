package fullcare.backend.oauth2;

import fullcare.backend.member.domain.Member;
import fullcare.backend.member.domain.MemberRole;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.oauth2.domain.OAuth2Attributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;


/**
 * The type Custom o auth 2 user service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService 진입");

        // authorization code를 이용하여 이미 access token을 받았고, 해당 access 토큰은 userRequest에 저장되어있음.
        OAuth2AccessToken accessToken = userRequest.getAccessToken();

        // 토큰 정보 로깅
        tokenInfo(accessToken);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        log.info("registrationId : {}", registrationId);
        log.info("userNameAttributeName : {}", userNameAttributeName);


        // access 토큰을 이용하여 사용자 정보 갖고오기
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // OAuth2 Provider로부터 가져온 사용자 정보
        String name = oAuth2User.getName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();

        // 사용자 정보 로깅
        oAuth2UserInfo(name, attributes, authorities);

        // 사용자 정보를 토대로, Member 엔티티를 만드는데 필요한 정보만 추출
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, attributes);

        // OAuth2Attributes와 registrationId 를 이용하여 기존 사용자가 존재하는지 찾고, 없다면 새로 만들어서 반환
        Member loginMember = getMember(oAuth2Attributes, registrationId);

        System.out.println("loginMember = " + loginMember);


        // todo loginMember를 OAuth2User로 변환하여 return 해야함


        // 여기서 반환한 객체가 OAuth2SuccessHandler의 authentication로 전달된다.
        return new DefaultOAuth2User(authorities,attributes,userNameAttributeName);
    }

    private Member getMember(OAuth2Attributes oAuth2Attributes, String oAuth2ProviderName) {
        String oAuth2Id = oAuth2ProviderName + oAuth2Attributes.getOAuth2UserInfo().getId();
        Member findMember = memberRepository.findByoAuth2Id(oAuth2Id).orElse(null);

        if (findMember == null){
            log.info("Create New Member!");
            return newMember(oAuth2Attributes, oAuth2ProviderName);
        }

        return findMember;
    }

    private Member newMember(OAuth2Attributes oAuth2Attributes, String oAuth2providerName) {
        Member newMember = oAuth2Attributes.toEntity(oAuth2providerName, MemberRole.GUEST);

        return memberRepository.save(newMember);
    }

    private void oAuth2UserInfo(String name, Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities) {
        log.info("OAuth2User Info ================================");
        log.info("OAuth2User name : {}", name);
        log.info("OAuth2User attributes : {}", attributes);
        log.info("OAuth2User authorities : {}", authorities);
        log.info("=======================================================");
    }

    private void tokenInfo(OAuth2AccessToken accessToken) {
        log.info("OAuth2AccessToken Info ================================");
        log.info("OAuth2AccessToken Type : {}", accessToken.getTokenType().getValue());
        log.info("OAuth2AccessToken Value : {}", accessToken.getTokenValue());
        log.info("OAuth2AccessToken IssuedAt : {}", accessToken.getIssuedAt());
        log.info("OAuth2AccessToken ExpiresAt : {}", accessToken.getExpiresAt());
        log.info("OAuth2AccessToken Scope : {}", accessToken.getScopes());
        log.info("=======================================================");
    }
}
