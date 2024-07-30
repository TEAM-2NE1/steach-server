package com.twentyone.steachserver.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HealthController {
    @GetMapping("/check/server/operating")
    public ResponseEntity<String> CheckServerOperating() {
        return ResponseEntity.ok("OK");
    }
}
