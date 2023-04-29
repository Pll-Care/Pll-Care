package fullcare.backend.oauth2.domain;

import java.util.Map;


// todo 각 getter별 NPE 방지용 검증코드가 필요함
public class NaverOAuthUser2Info extends OAuth2UserInfo{

    public NaverOAuthUser2Info(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return (String) response.get("id");
    }

    @Override
    public String getName() {
        Map<String, Object> tmp = (Map<String, Object>) attributes.get("response");

        return (String) tmp.get("name");
    }

    @Override
    public String getEmail() {
        Map<String, Object> tmp = (Map<String, Object>) attributes.get("response");

        return (String) tmp.get("email");
    }
}
