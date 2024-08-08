package com.twentyone.steachserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8080")
                        .allowedOrigins("http://localhost:5173")
                        .allowedOrigins("http://steach.ssafy.io:5173")
                        .allowedOrigins("https://steach.ssafy.io:5173")
                        .allowedOrigins("http://localhost/")
                        .allowCredentials(true)
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }

    // CORS 허용 적용
    @Bean
    @Primary
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedOrigin("http://localhost");
        configuration.addAllowedOrigin("http://localhost/");

        configuration.addAllowedOrigin("http://steach.ssafy.io:5173");
        configuration.addAllowedOrigin("https://steach.ssafy.io:5173");

        configuration.addAllowedOrigin("http://localhost:80");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("http://localhost:18008");
        configuration.addAllowedOrigin("http://localhost:18080");
        configuration.addAllowedOrigin("http://localhost:18081");

        // nginx꺼
//        Todo: 왜 check/server 는 그냥 돼?
        configuration.addAllowedOrigin("https://localhost:8088");
        // 몰라?
        configuration.addAllowedOrigin("https://43.202.1.52:8088");
        // 스웨거?
        configuration.addAllowedOrigin("https://43.202.1.52:18080");
        configuration.addAllowedOrigin("https://steach.ssafy.io:18081");
        configuration.addAllowedOrigin("https://14.46.142.208:18080");


        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

