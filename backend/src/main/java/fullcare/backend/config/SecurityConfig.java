package fullcare.backend.config;

import fullcare.backend.security.oauth2.CustomOAuth2UserService;
import fullcare.backend.security.oauth2.OAuth2FailureHandler;
import fullcare.backend.security.oauth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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


    // * 정적 자원을 백엔드 서버에서 제공하는 경우 시큐리티 필터를 거치지 않게하기 위해 사용
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web
                .ignoring().requestMatchers("/**", "/swagger-ui/**");
    }


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
                        .requestMatchers("/oauth2/authorization/**").permitAll()
                        // * 추후에 접근을 열어야하는 경로가 있다면 추가
                        .anyRequest().denyAll()) // ! 나머지 요청들은 접근 자체를 막아야함
                        .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)); // * 실제로 사용되지는 않지만, DefaultLoginPageGeneratingFilter를 없애줌
//                        .accessDeniedHandler(new SimpleAccessDeniedHandler());
        // !별도의 AccessDeniedHandler 지정 안할 시, /error 로 재요청함/


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
        http.httpBasic().disable().formLogin().disable().anonymous().disable()
                .csrf().disable().headers().frameOptions().disable();


        return http.build();
    }


}