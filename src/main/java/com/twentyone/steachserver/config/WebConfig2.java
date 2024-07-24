//package com.twentyone.steachserver.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig2 implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:8080")
//                .allowedOrigins("http://localhost:5173")
//                .allowedOrigins("http://localhost/")
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "PATCH", "DELETE", "PUT",
//                        HttpMethod.OPTIONS.name()
//                );
//    }
//}
