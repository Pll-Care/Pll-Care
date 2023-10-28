package fullcare.backend.security.oauth2.domain;

import java.util.Map;


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

    @Override
    public String getImageUrl() {
        Map<String, Object> tmp = (Map<String, Object>) attributes.get("response");

        return (String) tmp.get("profile_image");
    }
}
