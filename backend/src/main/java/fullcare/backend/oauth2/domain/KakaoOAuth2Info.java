package fullcare.backend.oauth2.domain;

import java.util.Map;

// todo 각 getter별 NPE 방지용 검증코드가 필요함
public class KakaoOAuth2Info extends OAuth2UserInfo{
    public KakaoOAuth2Info(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getName() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return (String) profile.get("nickname");
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        return (String) kakaoAccount.get("email");
    }
}
