package com.twentyone.steachserver.integration;

import org.junit.jupiter.api.Nested;
import org.springframework.test.context.ActiveProfiles;

// 통합 테스트는 시스템의 개별 모듈이 서로 올바르게 상호작용하는지를 확인하는 테스트입니다.
// 회원 가입 및 회원 정보 수정 프로세스에서 여러 모듈 (예: 데이터베이스, 인증 시스템, 사용자 인터페이스 등)이 상호작용하는 것을 테스트합니다.
@Nested
@ActiveProfiles("test")
public abstract class IntegrationTest {
}
