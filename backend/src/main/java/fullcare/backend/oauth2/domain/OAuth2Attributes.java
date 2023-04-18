package fullcare.backend.oauth2.domain;


import fullcare.backend.member.domain.Member;
import fullcare.backend.member.domain.MemberRole;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;
@Getter
@Slf4j
@Builder
public class OAuth2Attributes {

    private String nameAttributeKey; // OAuth2 로그인 성공했을 때, Provider가 사용자를 식별하는 값을 가리키는 키
    private OAuth2UserInfo oAuth2UserInfo;

    public static OAuth2Attributes of(String oAuth2ProviderName, String nameAttributeKey,
                                      Map<String, Object> attributes) {

        return switch (oAuth2ProviderName) {
            case "google" -> ofGoogle(nameAttributeKey, attributes);
            case "naver" -> ofNaver(nameAttributeKey, attributes);
            case "kakao" -> ofKakao(nameAttributeKey, attributes);
            default -> throw new IllegalArgumentException("Not Supported Provider");
        };
    }

    private static OAuth2Attributes ofGoogle(String nameAttributeKey, Map<String, Object> attributes) {

        return OAuth2Attributes.builder()
                .nameAttributeKey(nameAttributeKey)
                .oAuth2UserInfo(new GoogleOAuth2Info(attributes))
                .build();

    }

    private static OAuth2Attributes ofNaver(String nameAttributeKey, Map<String, Object> attributes) {

        return OAuth2Attributes.builder()
                .nameAttributeKey(nameAttributeKey)
                .oAuth2UserInfo(new NaverOAuth2Info(attributes))
                .build();

    }

    private static OAuth2Attributes ofKakao(String nameAttributeKey, Map<String, Object> attributes) {

        return OAuth2Attributes.builder()
                .nameAttributeKey(nameAttributeKey)
                .oAuth2UserInfo(new KakaoOAuth2Info(attributes))
                .build();
    }

    public Member toEntity(String oAuth2ProviderName, MemberRole role) {
        return Member.builder()
                .oAuth2Id(oAuth2ProviderName + this.oAuth2UserInfo.getId() )
                .nickname(null)
                .name(this.oAuth2UserInfo.getName())
                .authority(role.toString())
                .signupDate(LocalDateTime.now())
                .email(this.oAuth2UserInfo.getEmail())
                .build();
    }
}
