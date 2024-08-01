package com.twentyone.steachserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity // @Secured 를 위한 어노테이션
@EnableJpaAuditing // BaseTimeEntity 사용 위한 어노테이션
@EnableCaching // 캐싱을 사용하기 위해서
@EnableScheduling // 스케줄링 사용을 위해서
@SpringBootApplication
public class SteachServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SteachServerApplication.class, args);
    }

}
