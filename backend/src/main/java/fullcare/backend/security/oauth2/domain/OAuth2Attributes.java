package fullcare.backend.security.oauth2.domain;


import fullcare.backend.member.domain.Member;
import fullcare.backend.member.domain.MemberRole;
import fullcare.backend.profile.domain.Profile;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Slf4j
public class OAuth2Attributes {

    private OAuth2UserInfo oAuth2UserInfo;

    @Builder
    public OAuth2Attributes(OAuth2UserInfo oAuth2UserInfo) {
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    public static OAuth2Attributes of(String oAuth2ProviderName, Map<String, Object> attributes) {

        return switch (oAuth2ProviderName) {
            case "google" -> ofGoogle(attributes);
            case "naver" -> ofNaver(attributes);
            case "kakao" -> ofKakao(attributes);
            default -> throw new IllegalArgumentException("Not Supported Provider");
        };
    }

    private static OAuth2Attributes ofGoogle(Map<String, Object> attributes) {

        return OAuth2Attributes.builder()
                .oAuth2UserInfo(new GoogleOAuthUser2Info(attributes))
                .build();
    }

    private static OAuth2Attributes ofNaver(Map<String, Object> attributes) {

        return OAuth2Attributes.builder()
                .oAuth2UserInfo(new NaverOAuthUser2Info(attributes))
                .build();
    }

    private static OAuth2Attributes ofKakao(Map<String, Object> attributes) {

        return OAuth2Attributes.builder()
                .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public Member toMemberEntity(String oAuth2ProviderName, MemberRole role) {
        return Member.builder()
                .oAuth2Id(oAuth2ProviderName + "_" + this.oAuth2UserInfo.getId())
                .nickname("nickname_" + this.oAuth2UserInfo.getName())
                .name(this.oAuth2UserInfo.getName())
                .role(role)
                .signupDate(LocalDateTime.now())
                .email(this.oAuth2UserInfo.getEmail())
                .imageUrl(this.oAuth2UserInfo.getImageUrl())
                .profile(new Profile("한 줄 소개입니다."))
                .build();
    }
}
