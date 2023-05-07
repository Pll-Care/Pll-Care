package fullcare.backend.security.oauth2.domain;

import fullcare.backend.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User, UserDetails {

    private final Long id;
    private final String nickname;
    private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    public CustomOAuth2User(Long id, String nickname, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.nickname = nickname;
        this.authorities = authorities;
    }

    public static CustomOAuth2User create(Member member) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(member.getRole().getValue()));
        return new CustomOAuth2User(member.getId(), member.getNickname(), authorities);
    }
    public static CustomOAuth2User create(Member loginMember, Map<String, Object> attributes) {
        CustomOAuth2User oAuth2User = CustomOAuth2User.create(loginMember);
        oAuth2User.setAttributes(attributes);
        return oAuth2User;
    }
    private void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return nickname;
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
