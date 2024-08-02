package com.twentyone.steachserver.config;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * @ActiveProfiles("test")가 사용되면, Spring Boot는 test 프로파일을 활성화합니다. 이때 다음과 같은 순서로 설정 파일이 로드됩니다:
 *
 * application.yml 또는 application.properties
 * application-test.yml 또는 application-test.properties (활성화된 프로파일에 따라 로드)
 * 따라서 @ActiveProfiles("test")가 붙어 있는 경우, application-test.yml 파일이 로드되어 해당 프로파일에 대한 설정이 적용됩니다.
 */
@ActiveProfiles("test")
@TestConfiguration
@DisplayName("Redis Test Containers")
public class RedisTestContainers {

    private static final String REDIS_DOCKER_IMAGE = "redis:7.4.0-alpine";

    static {
        System.out.println("Redis Test Container Start");// (1)
        GenericContainer<?> REDIS_CONTAINER =
                new GenericContainer<>(DockerImageName.parse(REDIS_DOCKER_IMAGE))
                        .withExposedPorts(6379)
                        .withReuse(true);

        REDIS_CONTAINER.start();    // (2)

        // (3)
        System.setProperty("spring.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
    }
}
