package fullcare.backend.config;

import fullcare.backend.security.jwt.JwtAccessDeniedHandler;
import fullcare.backend.security.jwt.JwtAuthenticationEntryPoint;
import fullcare.backend.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class JwtConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    @Bean
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
//        http
//        .cors().configurationSource(corsConfigurationSource());

        log.info("JwtConfig.jwtFilterChain");

        // ! cors 설정ㅓ
        http.cors(withDefaults());


        // ! session 미사용 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // ! 그 외의 부가 설정
        http.httpBasic().disable()
//                .anonymous().disable()
                .csrf().disable().headers().frameOptions().disable();

        // ! HTTP 경로 관련 설정
        http
                .securityMatcher("/api/auth/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "api/auth/main/**").hasAnyRole("USER", "ANONYMOUS")
                        .requestMatchers(HttpMethod.GET, "/api/auth/post/list", "/api/auth/post/{postId:[\\d+]}").hasAnyRole("USER", "ANONYMOUS")
                        .requestMatchers(HttpMethod.GET, "api/auth/util/techstack").hasAnyRole("USER", "ANONYMOUS")
                        .requestMatchers(HttpMethod.GET, "/api/auth/profile/{memberId:[\\d+]}/rolestack",
                                "/api/auth/profile/{memberId:[\\d+]}/experience", "/api/auth/profile/{memberId:[\\d+]}/evaluation",
                                "/api/auth/profile/{memberId:[\\d+]}/evaluation/{projectId:[\\d+]}", "/api/auth/profile/{memberId:[\\d+]}/evaluation/chart",
                                "/api/auth/profile/{memberId:[\\d+]}/contact", "/api/auth/profile/{memberId:[\\d+]}/bio").hasAnyRole("USER", "ANONYMOUS")
                        .requestMatchers("/api/auth/**").hasRole("USER")
                        .anyRequest().authenticated())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
//                .accessDeniedHandler(jwtAccessDeniedHandler);


        return http.build();
    }
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource(){
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedOriginPattern("http://localhost:3000");
//
//        configuration.addAllowedMethod("*");
//        configuration.addAllowedHeader("*");
//        configuration.setMaxAge(7200L);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**",configuration);
//        return source;
//    }
}