package fullcare.backend.security.oauth2.domain;

import fullcare.backend.member.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User, UserDetails {

    private final Member member;
    private Map<String, Object> attributes;

    public CustomOAuth2User(Member member) {
        this.member = member;
    }

    public static CustomOAuth2User create(Member member) {
        return new CustomOAuth2User(member);
    }

    public static CustomOAuth2User create(Member member, Map<String, Object> attributes) {
        CustomOAuth2User oAuth2User = CustomOAuth2User.create(member);
        oAuth2User.setAttributes(attributes);
        return oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    private void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(member.getRole().getValue()));
    }

    @Override
    public String getName() {
        return String.valueOf(this.member.getId());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.member.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
