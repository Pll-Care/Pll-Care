package fullcare.backend.config;

import fullcare.backend.security.oauth2.CustomOAuth2UserService;
import fullcare.backend.security.oauth2.OAuth2FailureHandler;
import fullcare.backend.security.oauth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;

    @Bean
    public SecurityFilterChain globalFilterChain(HttpSecurity http) throws Exception {

        // ! cors 설정
        http.cors(withDefaults());


        // ! session 미사용 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        // ! HTTP 경로 관련 설정
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login/oauth2/**").permitAll()
                        .requestMatchers("/authorization/oauth2/**").permitAll()
                        .anyRequest().denyAll())
                        .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));


        // ! OAuth2 로그인 관련 설정
        http
                .oauth2Login()
                .redirectionEndpoint().baseUri("/login/oauth2/**")
                .and()
                .userInfoEndpoint().userService(oAuth2UserService)
                .and()
                .successHandler(successHandler)
                .failureHandler(failureHandler);


        // ! 그 외의 부가 설정
        http.httpBasic().disable().formLogin().disable()
                .csrf().disable().headers().frameOptions().disable();


        return http.build();
    }


}