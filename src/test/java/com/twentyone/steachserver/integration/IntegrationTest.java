package com.twentyone.steachserver.integration;

import com.twentyone.steachserver.SteachTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;

// 통합 테스트는 시스템의 개별 모듈이 서로 올바르게 상호작용하는지를 확인하는 테스트입니다.
// 회원 가입 및 회원 정보 수정 프로세스에서 여러 모듈 (예: 데이터베이스, 인증 시스템, 사용자 인터페이스 등)이 상호작용하는 것을 테스트합니다.

/**
 * @AutoConfigureMockMvc
 * 역할: MockMvc를 자동으로 구성합니다.
 * 설명: MockMvc를 자동으로 설정하여 웹 애플리케이션의 컨트롤러 테스트를 쉽게 할 수 있게 합니다. 이를 통해 HTTP 요청 및 응답을 시뮬레이션하여 테스트할 수 있습니다.
 * @RequiredArgsConstructor
 * 역할: Lombok 라이브러리의 어노테이션으로, final 필드와 @NonNull 필드를 포함하는 생성자를 자동으로 생성합니다.
 * 설명: 의존성 주입을 위해 final 필드와 @NonNull 필드를 포함한 생성자를 자동으로 생성해 줍니다. 이를 통해 필드 주입 대신 생성자 주입을 사용할 수 있으며, 생성자를 통해 필요한 의존성을 주입받을 수 있습니다.
 */

// @SpringBootTest 기본적으로 MOCK 환경에서 애플리케이션 컨텍스트를 로드합니다.
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) 내장 서버를 임의의 포트에서 시작하여 실제 네트워크 요청을 테스트할 수 있습니다.
@Transactional
@SpringBootTest
public abstract class IntegrationTest extends SteachTest {

}
