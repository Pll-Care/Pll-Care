package fullcare.backend.security.oauth2;

import fullcare.backend.member.domain.Member;
import fullcare.backend.member.domain.MemberRole;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.security.oauth2.domain.CustomOAuth2User;
import fullcare.backend.security.oauth2.domain.OAuth2Attributes;
import fullcare.backend.security.oauth2.domain.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;


/**
 * The type Custom o auth 2 user service.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    @Transactional // todo @Transactional을 여기에다만 붙이면 되는지는 고민을 좀 해봐야할듯(트랜잭션 전파 측면 + 트랜잭션이 적용되는 범위가 너무 넓음)
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        log.info("CustomOAuth2UserService 진입");

        // * userRequest의 accessToken으로 OAuth2 사용자 정보를 받아와야함
        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        tokenInfo(accessToken);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        log.info("registrationId : {}", registrationId);
        log.info("userNameAttributeName : {}", userNameAttributeName);


        // * access 토큰을 이용하여 사용자 정보 갖고오기
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        // * OAuth2 Provider로부터 가져온 OAuth2 사용자 정보
        String name = oAuth2User.getName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();

        oAuth2UserInfo(name, attributes, authorities);

        // * OAuth2 사용자 정보를 토대로, Member 엔티티를 만드는데 필요한 정보만 추출
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, attributes);

        //  * OAuth2Attributes와 registrationId 를 이용하여 기존 사용자가 존재하는지 찾고, 없다면 새로 만들어서 반환
        // todo 최초 로그인 회원과 기존 회원을 어떻게 구분할 것인가?
        Member loginMember = getMember(oAuth2Attributes, registrationId);

        // todo loginMember를 CustomOAuth2User로 변환하여 반환
        // * 여기서 반환한 객체가 OAuth2SuccessHandler의 authentication로 전달된다.
        return CustomOAuth2User.create(loginMember,oAuth2User.getAttributes());
    }

    private Member getMember(OAuth2Attributes oAuth2Attributes, String oAuth2ProviderName) {
        String oAuth2Id = oAuth2ProviderName + "_" + oAuth2Attributes.getOAuth2UserInfo().getId();
        log.info("oAuth2Id = {}",oAuth2Id);

        Member findMember = memberRepository.findByoAuth2Id(oAuth2Id).orElse(null);

        log.info("findMember : {}", findMember);

        if (findMember == null){
            return newMember(oAuth2Attributes, oAuth2ProviderName);
        }

        // todo updateMember()를 진짜 달라진 경우에만 호출하는 방식으로 고쳐야한다.
        updateMember(findMember, oAuth2Attributes,oAuth2ProviderName);
        return findMember;
    }


    private void updateMember(Member member, OAuth2Attributes oAuth2Attributes, String oAuth2ProviderName) {
        String oAuth2Id = oAuth2ProviderName + "_"+ oAuth2Attributes.getOAuth2UserInfo().getId();
        member.updateOAuth2Id(oAuth2Id);
        member.updateName(oAuth2Attributes.getOAuth2UserInfo().getName());
        member.updateEmail(oAuth2Attributes.getOAuth2UserInfo().getEmail());

    }

    private Member newMember(OAuth2Attributes oAuth2Attributes, String oAuth2providerName) {
        Member newMember = oAuth2Attributes.toEntity(oAuth2providerName, MemberRole.USER);

        return memberRepository.save(newMember);
    }

    // * 사용자 정보 로깅
    private void oAuth2UserInfo(String name, Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities) {
        log.info("OAuth2User Info ================================");
        log.info("OAuth2User name : {}", name);
        log.info("OAuth2User attributes : {}", attributes);
        log.info("OAuth2User authorities : {}", authorities);
        log.info("=======================================================");
    }

    // * OAuth2Access 토큰 정보 로깅
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
