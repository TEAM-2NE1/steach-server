package com.twentyone.steachserver.acceptance;

import com.twentyone.steachserver.SteachTest;
import com.twentyone.steachserver.helper.DatabaseCleanup;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import io.restassured.RestAssured;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static io.restassured.RestAssured.UNDEFINED_PORT;


/**
 * 테스트 클래스가 추상 클래스인 이유는 여러 이유가 있을 수 있습니다. 아래에서 그 이유를 설명하겠습니다:
 * <p>
 * 1. 공통 설정 및 초기화 코드 공유
 * 추상 클래스를 사용하면 여러 테스트 클래스가 공통으로 사용하는 설정 및 초기화 코드를 한 곳에 모아서 관리할 수 있습니다. 이렇게 하면 코드의 중복을 줄이고, 유지보수를 용이하게 할 수 있습니다.
 * <p>
 * 2. 상속을 통한 기능 확장
 * 다양한 테스트 클래스가 이 추상 클래스를 상속받아 기본 설정을 공유하면서도 각 테스트 클래스별로 고유한 테스트를 추가할 수 있습니다. 즉, 추상 클래스는 기본 틀을 제공하고, 구체적인 테스트 클래스들은 그 틀을 확장하여 자신만의 테스트를 작성할 수 있습니다.
 * <p>
 * 3. 코드의 일관성 유지
 * 모든 테스트 클래스가 동일한 초기화 및 설정 절차를 따르도록 강제할 수 있습니다. 이렇게 하면 테스트 환경이 일관되게 유지되어 테스트 결과의 신뢰성을 높일 수 있습니다.
 *
 * @SpringBootTest(webEnvironment = RANDOM_PORT)는 통합 테스트를 위해 전체 애플리케이션 컨텍스트를 로드하고, 임의의 포트에서 애플리케이션을 실행하여 테스트 환경을 설정합니다. 이는 실제 운영 환경과 유사한 조건에서 애플리케이션을 테스트할 수 있게 하며, 포트 충돌을 피할 수 있도록 합니다.
 * 이 예제에서는 @SpringBootTest(webEnvironment = RANDOM_PORT)를 사용하여 임의의 포트에서 서버를 시작하고, @LocalServerPort를 통해 해당 포트를 주입받아 테스트에 사용하고 있습니다. TestRestTemplate을 사용하여 실제 서버에 요청을 보내고 응답을 검증합니다.
 * 다른 webEnvironment 옵션
 * webEnvironment 속성은 여러 가지 값을 가질 수 있습니다:
 * <p>
 * MOCK (기본값):
 * <p>
 * 서블릿 환경을 모킹하여 실제 서버를 시작하지 않습니다.
 * 주로 컨트롤러 레벨의 테스트에 사용됩니다.
 * DEFINED_PORT:
 * <p>
 * 기본적으로 정의된 포트(일반적으로 application.properties에 설정된 포트)에서 서버를 시작합니다.
 * 특정 포트에서 테스트를 수행해야 할 때 사용됩니다.
 * RANDOM_PORT:
 * <p>
 * 임의의 포트에서 서버를 시작합니다.
 * 포트 충돌을 피하고 격리된 테스트 환경을 만들 때 유용합니다.
 * NONE:
 * <p>
 * 웹 환경을 시작하지 않습니다.
 * 일반적인 스프링 컨텍스트를 로드하여 비웹 애플리케이션을 테스트할 때 사용됩니다.
 * @SpringBootTest(webEnvironment = RANDOM_PORT)는 통합 테스트를 위해 전체 애플리케이션 컨텍스트를 로드하고, 임의의 포트에서 애플리케이션을 실행하여 테스트 환경을 설정합니다. 이는 실제 운영 환경과 유사한 조건에서 애플리케이션을 테스트할 수 있게 하며, 포트 충돌을 피할 수 있도록 합니다.
 * 이 예제에서는 @SpringBootTest(webEnvironment = RANDOM_PORT)를 사용하여 임의의 포트에서 서버를 시작하고, @LocalServerPort를 통해 해당 포트를 주입받아 테스트에 사용하고 있습니다. TestRestTemplate을 사용하여 실제 서버에 요청을 보내고 응답을 검증합니다.
 * 다른 webEnvironment 옵션
 * webEnvironment 속성은 여러 가지 값을 가질 수 있습니다:
 * <p>
 * MOCK (기본값):
 * <p>
 * 서블릿 환경을 모킹하여 실제 서버를 시작하지 않습니다.
 * 주로 컨트롤러 레벨의 테스트에 사용됩니다.
 * DEFINED_PORT:
 * <p>
 * 기본적으로 정의된 포트(일반적으로 application.properties에 설정된 포트)에서 서버를 시작합니다.
 * 특정 포트에서 테스트를 수행해야 할 때 사용됩니다.
 * RANDOM_PORT:
 * <p>
 * 임의의 포트에서 서버를 시작합니다.
 * 포트 충돌을 피하고 격리된 테스트 환경을 만들 때 유용합니다.
 * NONE:
 * <p>
 * 웹 환경을 시작하지 않습니다.
 * 일반적인 스프링 컨텍스트를 로드하여 비웹 애플리케이션을 테스트할 때 사용됩니다.
 */

/**
 * @SpringBootTest(webEnvironment = RANDOM_PORT)는 통합 테스트를 위해 전체 애플리케이션 컨텍스트를 로드하고, 임의의 포트에서 애플리케이션을 실행하여 테스트 환경을 설정합니다. 이는 실제 운영 환경과 유사한 조건에서 애플리케이션을 테스트할 수 있게 하며, 포트 충돌을 피할 수 있도록 합니다.
 * 이 예제에서는 @SpringBootTest(webEnvironment = RANDOM_PORT)를 사용하여 임의의 포트에서 서버를 시작하고, @LocalServerPort를 통해 해당 포트를 주입받아 테스트에 사용하고 있습니다. TestRestTemplate을 사용하여 실제 서버에 요청을 보내고 응답을 검증합니다.
 * 다른 webEnvironment 옵션
 * webEnvironment 속성은 여러 가지 값을 가질 수 있습니다:
 *
 * MOCK (기본값):
 *
 * 서블릿 환경을 모킹하여 실제 서버를 시작하지 않습니다.
 * 주로 컨트롤러 레벨의 테스트에 사용됩니다.
 * DEFINED_PORT:
 *
 * 기본적으로 정의된 포트(일반적으로 application.properties에 설정된 포트)에서 서버를 시작합니다.
 * 특정 포트에서 테스트를 수행해야 할 때 사용됩니다.
 * RANDOM_PORT:
 *
 * 임의의 포트에서 서버를 시작합니다.
 * 포트 충돌을 피하고 격리된 테스트 환경을 만들 때 유용합니다.
 * NONE:
 *
 * 웹 환경을 시작하지 않습니다.
 * 일반적인 스프링 컨텍스트를 로드하여 비웹 애플리케이션을 테스트할 때 사용됩니다.
 */

/**
 * @SpringBootTest
 * 역할: 스프링 부트 애플리케이션 컨텍스트를 로드합니다.
 * 설명: 통합 테스트를 실행하기 위해 전체 스프링 부트 애플리케이션 컨텍스트를 로드합니다. 이를 통해 실제 애플리케이션 환경에서 테스트를 수행할 수 있습니다. 모든 빈이 로드되고, 실제 애플리케이션과 유사한 환경에서 테스트가 실행됩니다.
 * @TestMethodOrder(OrderAnnotation.class)
 * 역할: 테스트 메서드의 실행 순서를 지정합니다.
 * 설명: OrderAnnotation을 사용하면 @Order 어노테이션을 통해 각 테스트 메서드의 실행 순서를 명시적으로 정의할 수 있습니다. 이를 통해 특정 순서대로 테스트를 실행할 수 있습니다.
 * @TestInstance(TestInstance.Lifecycle.PER_CLASS)를 사용하지 않으면, 각 테스트 메서드는 독립적으로 실행되며, 테스트 클래스의 인스턴스는 각 테스트 메서드마다 새로 생성됩니다.
 * 이는 테스트 메서드가 인스턴스 변수의 상태를 공유하지 않음을 의미합니다.
 * @TestInstance(TestInstance.Lifecycle.PER_CLASS) 역할: JUnit 5에서 테스트 클래스의 인스턴스 생명주기를 제어합니다.
 * 설명: Lifecycle.PER_CLASS를 사용하면 테스트 클래스의 인스턴스가 클래스당 하나만 생성됩니다.
 * 기본적으로 JUnit 5는 각 테스트 메서드마다 새로운 테스트 클래스 인스턴스를 생성하지만, PER_CLASS를 사용하면 테스트 클래스당 하나의 인스턴스만 생성되므로 테스트 메서드 간의 상태를 공유할 수 있습니다.
 *
 * /

// 보스독님의 블로그에서 인수 테스트는 Mock 프레임워크를 사용하지 않고 최대한 실 서비스 환경과 동일한 환경에서 테스트 되어야 한다는 것을 알게 되었다
// 서비스 디비(X) Mock객체로 인한 가짜 디비로 테스트가 아닌 진짜 DB로 해야함.

// 인수테스트는 어떤 시스템이 실제 운영 환경에서 사용될 준비가 되었는지 최종적으로 확인하는 단계로, 체크리스트를 통해 비즈니스 관점에서의 사용 시나리오별로 동작을 검증한다.
// 결국 누가, 어떤 목적으로, 무엇을 하는가를 테스트 하는 것이다.
// 보통 내부적인 구조나, 구현 방법을 고려한다기 보다는 사용자의 관점에 집중하는 경우가 많다. "어떻게"에는 관심이 없고, A를 했을 때 B 일이 일어나면 되는 것이다.
// 그래서 자동화 인수 테스트는 거진 E2E 테스트와 동일한 형태로 작성된다
// End to End Test는 "끝에서 끝까지" 테스트 한다는 의미로, 어떤 기능이 동작하기 위해 참여하는 모든 컴포넌트들이 서로 올바르게 협업해 원하는 결과를 반환하는지 확인하는 테스트이다.
// 그리고 보통 예외적인 상황 보다는 Happy Case를 작성한다. (제대로 작동할 때의 동작)
// 인수 테스트 : 비즈니스적으로 클라이언트의 요구사항을 제대로 만족하는지? (인수하기 위한 테스트!)
// E2E 테스트 : 기술적인 관점에서 어떤 기능이 제대로 작동하는지? 참여하는 모든 컴포넌트들이 제대로 상호작용을 해내서 원하는 결과를 반환하는지?

// RestAssured를 활용해서 테스트 코드를 작성하는 방식을 많이 보았다.

//ATDD

/**
 * 결론부터 말하면 @Transactional 어노테이션을 사용해서 트랜잭션을 롤백하는 전략은 인수 테스트에서는 사용할 수 없다.
 * 아마 많은 사람들이 테스트 프레임워크에서 관리하는 @Transactional 어노테이션을 붙이면 트랜잭션이 끝난 뒤 롤백된다고 알고 있다. 물론 틀린 말은 아니다.
 * 하지만, 인수 테스트의 경우 @SpringBootTest 어노테이션에 port를 지정하여 서버를 띄우게 되는 데 이때, HTTP 클라이언트와 서버는 각각 다른 스레드에서 실행된다.
 * 따라서 아무리 테스트 코드에 @Transactional 어노테이션이 있다고 하더라도 호출되는 쪽은 다른 스레드에서 새로운 트랜잭션으로 커밋하기 때문에 롤백 전략이 무의미해지는 것이다.
 */
//@Transactional

// 수용 테스트는 시스템이 최종 사용자의 요구사항을 충족하는지를 확인하는 테스트입니다.
// 이는 전체 시스템이 기대한 대로 동작하는지, 사용자의 시나리오를 통해 확인합니다.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest extends SteachTest {

    @LocalServerPort
    protected int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    // 해당 메서드를 구현체에 한 번 더 적어줄 필요가 없음.
    @BeforeAll
    void setUp() {
        // 설명: 이 조건문은 RestAssured의 포트가 아직 설정되지 않았는지를 확인합니다.
        // 목적: 테스트 실행 시, RestAssured의 포트가 아직 설정되지 않았다면, 이를 LocalServerPort에서 가져온 포트로 설정하려는 의도입니다.
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port;
            databaseCleanup.afterPropertiesSet();
        }
        databaseCleanup.execute();
    }
}
