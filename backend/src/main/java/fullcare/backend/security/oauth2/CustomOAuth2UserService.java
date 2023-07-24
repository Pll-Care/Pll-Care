package fullcare.backend.security.oauth2;

import fullcare.backend.member.domain.Member;
import fullcare.backend.member.domain.MemberRole;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.security.oauth2.domain.CustomOAuth2User;
import fullcare.backend.security.oauth2.domain.OAuth2Attributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        log.info("CustomOAuth2UserService 진입");

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        log.info("registrationId : {}", registrationId);
        log.info("userNameAttributeName : {}", userNameAttributeName);


        // * access 토큰을 이용하여 사용자 정보 갖고오기
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // * OAuth2 사용자 정보를 토대로, Member 엔티티를 만드는데 필요한 정보만 추출
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, attributes);

        //  * OAuth2Attributes와 registrationId 를 이용하여 기존 사용자가 존재하는지 찾고, 없다면 새로 만들어서 반환
        // todo 최초 로그인 회원과 기존 회원을 어떻게 구분할 것인가?
        Member loginMember = getMember(oAuth2Attributes, registrationId);

        // todo loginMember를 CustomOAuth2User로 변환하여 반환
        // * 여기서 반환한 객체가 OAuth2SuccessHandler의 authentication로 전달된다.
        return CustomOAuth2User.create(loginMember, attributes);
    }

    private Member getMember(OAuth2Attributes oAuth2Attributes, String oAuth2ProviderName) {
        String oAuth2Id = oAuth2ProviderName + "_" + oAuth2Attributes.getOAuth2UserInfo().getId();
        log.info("oAuth2Id = {}", oAuth2Id);

        Optional<Member> findMember = memberRepository.findByoAuth2Id(oAuth2Id);
        log.info("findMember : {}", findMember);

        if (findMember.isEmpty()) {
            return newMember(oAuth2Attributes, oAuth2ProviderName);
        }

        // todo updateMember()를 진짜 달라진 경우에만 호출하는 방식으로 고쳐야한다.
        updateMember(findMember.get(), oAuth2Attributes, oAuth2ProviderName);
        return findMember.get();
    }


    private void updateMember(Member member, OAuth2Attributes oAuth2Attributes, String oAuth2ProviderName) {
        String oAuth2Id = oAuth2ProviderName + "_" + oAuth2Attributes.getOAuth2UserInfo().getId();
        member.updateOAuth2Id(oAuth2Id);
        member.updateName(oAuth2Attributes.getOAuth2UserInfo().getName());
        member.updateEmail(oAuth2Attributes.getOAuth2UserInfo().getEmail());
        member.updateImageUrl(oAuth2Attributes.getOAuth2UserInfo().getImageUrl());
    }

    private Member newMember(OAuth2Attributes oAuth2Attributes, String oAuth2providerName) {
        Member newMember = oAuth2Attributes.toEntity(oAuth2providerName, MemberRole.USER);
        return memberRepository.save(newMember);
    }
}
