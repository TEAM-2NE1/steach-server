package com.twentyone.steachserver.integration;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ControllerIntegrationTest extends IntegrationTest {
}
