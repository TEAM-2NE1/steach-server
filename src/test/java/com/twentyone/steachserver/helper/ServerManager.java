package com.twentyone.steachserver.helper;

import com.twentyone.steachserver.SteachServerApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ServerManager {

    private static final String SERVER_URL = "http://localhost:8080";

    public static boolean isServerRunning() {
        log.info("server running check : " + SERVER_URL + "/actuator/health");
        try {
            // RestTemplate 클래스는 Spring에서 제공하는 RESTful 웹 서비스와 상호작용하기 위한 간단한 HTTP 클라이언트입니다.
            RestTemplate restTemplate = new RestTemplate();
            // 서버 상태를 확인할 수 있는 엔드포인트
            ResponseEntity<String> response = restTemplate.getForEntity(SERVER_URL + "/actuator/health", String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }

    public static void startServer() {
        if (!isServerRunning()) {
            try {
                SpringApplication.run(SteachServerApplication.class);
            } catch (Exception e) {
                log.error("Failed to start server: ", e);
            }
        } else {
            log.info("Server is already running.");
        }
    }
}
