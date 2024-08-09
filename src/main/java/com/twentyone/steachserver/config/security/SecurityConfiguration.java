package com.twentyone.steachserver.config.security;

import com.twentyone.steachserver.domain.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfiguration {
    private static final String API_PREFIX = "/api/v1";

    private final JwtService jwtService;
    private final AuthenticationProvider authenticationProvider;
    private final UserDetailsService userDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    private final String[] swaggerWhiteList = {
            "/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**", "/error",
    };

    private final String[] whiteList = {
            API_PREFIX + "/login/**",
            API_PREFIX + "/*/join",
            API_PREFIX + "/check-username/*",
            API_PREFIX + "/student/check-nickname/*"
    };

    //TODO WebSecurityCustomizer로 whiteList 적용 알아보기
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(whiteList).permitAll()
                        .requestMatchers(swaggerWhiteList).permitAll()
                        .requestMatchers(HttpMethod.GET, API_PREFIX + "/curricula/**").permitAll()
                        .requestMatchers(HttpMethod.GET, API_PREFIX + "/main/**").permitAll()
                        .requestMatchers(HttpMethod.GET, API_PREFIX + "/lectures/*").permitAll()
                        .requestMatchers(HttpMethod.GET, API_PREFIX + "/teacher/check-email/*").permitAll()
                        .requestMatchers(HttpMethod.GET, API_PREFIX + "/student/check-email/*").permitAll()
                        .requestMatchers(HttpMethod.GET, API_PREFIX + "/check/server").permitAll()
                        .requestMatchers(HttpMethod.GET, API_PREFIX + "/teachers/*").permitAll()
                        .requestMatchers(HttpMethod.GET, API_PREFIX + "/teachers/curricula/*").permitAll()

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class);

        return http.build();
    }
}
