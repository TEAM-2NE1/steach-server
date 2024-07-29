package com.twentyone.steachserver.integration;

import org.springframework.boot.test.context.SpringBootTest;

// 하위 클래스에서 선언된 @SpringBootTest 애노테이션이 상위 클래스에서 선언된 것을 덮어씁니다.
// 따라서, ControllerIntegrationTest 클래스에는 하위 클래스에서 지정한 @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)가 적용됩니다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ControllerIntegrationTest extends IntegrationTest {
}
