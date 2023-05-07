package fullcare.backend.config;

import fullcare.backend.security.jwt.JwtAccessDeniedHandler;
import fullcare.backend.security.jwt.JwtAuthenticationEntryPoint;
import fullcare.backend.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
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

        // ! cors 설정
        http.cors(withDefaults());


        // ! session 미사용 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        // ! HTTP 경로 관련 설정
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").hasRole("USER")
                        .requestMatchers("api/all/**").permitAll())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
//                .accessDeniedHandler(jwtAccessDeniedHandler);


        // ! 그 외의 부가 설정
        http.httpBasic().disable()
//                .anonymous().disable()
                .csrf().disable().headers().frameOptions().disable();


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