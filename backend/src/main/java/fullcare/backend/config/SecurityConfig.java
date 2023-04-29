package fullcare.backend.config;

import fullcare.backend.oauth2.CustomOAuth2UserService;
import fullcare.backend.oauth2.OAuth2FailureHandler;
import fullcare.backend.oauth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Order(2)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors();


        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable();


        http
//                .securityMatcher("/error","/", "/test_lilac","/login/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll());


        http
                .oauth2Login()
                .redirectionEndpoint().baseUri("/login/oauth2/**")
                .and()
                .userInfoEndpoint().userService(oAuth2UserService)
                .and()
                .successHandler(successHandler)
                .failureHandler(failureHandler);


        return http.build();
    }


}