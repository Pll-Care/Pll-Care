package fullcare.backend.config;

import fullcare.backend.security.jwt.JwtAccessDeniedHandler;
import fullcare.backend.security.jwt.JwtAuthenticationEntryPoint;
import fullcare.backend.security.jwt.JwtAuthenticationFilter;
import fullcare.backend.security.oauth2.CustomOAuth2UserService;
import fullcare.backend.security.oauth2.OAuth2FailureHandler;
import fullcare.backend.security.oauth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class JwtConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;

    @Order(1)
    @Bean
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        http
                .securityMatcher("/api/**","/wow/**","/lilac/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").hasRole("USER")
                        .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/wow/**").denyAll()
                        .anyRequest().authenticated())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler());


        return http.build();
    }

}