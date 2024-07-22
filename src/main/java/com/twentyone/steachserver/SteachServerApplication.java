package com.twentyone.steachserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity // @Secured 를 위한 어노테이션
@EnableJpaAuditing // BaseTimeEntity 사용 위한 어노테이션
@SpringBootApplication
public class SteachServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SteachServerApplication.class, args);
    }

}
