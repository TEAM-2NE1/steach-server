package com.twentyone.steachserver.config.security;

import com.twentyone.steachserver.domain.auth.dto.JwtExceptionFilter;
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
    private final JwtService jwtService;
    private final AuthenticationProvider authenticationProvider;
    private final UserDetailsService userDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    private final String[] swaggerWhiteList = {
            "/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**", "/error",
    };

    private final String[] whiteList = {
            "/api/v1/login/**",
            "/api/v1/*/join",
            "/api/v1/check-username/*"
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
                        .requestMatchers(HttpMethod.GET, "/api/v1/curricula/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/lectures/*").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class);

        return http.build();
    }
}