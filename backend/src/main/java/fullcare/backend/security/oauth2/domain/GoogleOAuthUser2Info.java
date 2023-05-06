package fullcare.backend.security.oauth2.domain;

import java.util.Map;

public class GoogleOAuthUser2Info extends OAuth2UserInfo{

    public GoogleOAuthUser2Info(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {

        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}
