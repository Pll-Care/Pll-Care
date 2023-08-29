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

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(registrationId, attributes);

        Member loginMember = getMember(oAuth2Attributes, registrationId);
        return CustomOAuth2User.create(loginMember, attributes);
    }

    private Member getMember(OAuth2Attributes oAuth2Attributes, String oAuth2ProviderName) {
        String oAuth2Id = oAuth2ProviderName + "_" + oAuth2Attributes.getOAuth2UserInfo().getId();
        log.info("oAuth2Id = {}", oAuth2Id);

        Optional<Member> findMember = memberRepository.findByoAuth2Id(oAuth2Id);
        log.info("findMember : {}", findMember);

        if (findMember.isEmpty()) {
            return newMember(oAuth2Attributes, oAuth2ProviderName);
        } else {
            return updateMember(findMember.get(), oAuth2Attributes, oAuth2ProviderName);
        }
    }

    private Member updateMember(Member member, OAuth2Attributes oAuth2Attributes, String oAuth2ProviderName) {
        String oAuth2Id = oAuth2ProviderName + "_" + oAuth2Attributes.getOAuth2UserInfo().getId();

        member.updateOAuth2Id(oAuth2Id);
        member.updateName(oAuth2Attributes.getOAuth2UserInfo().getName());
        member.updateEmail(oAuth2Attributes.getOAuth2UserInfo().getEmail());

        return member;
    }

    private Member newMember(OAuth2Attributes oAuth2Attributes, String oAuth2providerName) {
        Member newMember = oAuth2Attributes.toMemberEntity(oAuth2providerName, MemberRole.USER);
        return memberRepository.save(newMember);
    }
}
