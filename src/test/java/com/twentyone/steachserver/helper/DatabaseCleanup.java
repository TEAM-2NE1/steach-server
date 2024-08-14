package com.twentyone.steachserver.helper;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 실제로 인수 테스트를 작성하면서 똑같은 테스트임에도 불구하고 테스트 격리가 잘되지 않아서 실행할 때마다 성공 여부가 달라지는 경험을 해본 적이 있을 것이다.
 * 이는 테스트가 진행되는 순서와 데이터베이스 초기 상태가 보장되지 않기 때문에 발생한 것이다.
 */

/**
 * TRUNCATE 쿼리는 앞서 살펴본 DELTE로 테이블을 초기화하는 방법보다는 상당히 괜찮은 방법이다. API 요청도 필요 없고, DELTE를 하기 위해서 하나씩 SELECT(조회)를 할 필요도 없다.
 * JPA의 경우 deleteAll, deleteById메서드를 호출하면 곧 바로 DELETE 쿼리가 수행되는 것이아니라 SELECT로 조회한 뒤에 DELETE가 나간다.
 * 그뿐만 아니라 삭제를 수행할 때 트랜잭션 로그 공간을 적게 사용하고, DELETE는 행마다 락(lock)을거는데 비해 TRUNCATE은 락(lock)을 거는 수가 상대적으로 적은 시간에 테이블 초기화를 할 수 있는 장점이 있다.
 */
@Service
@Profile("test") // ActiveProfile("test")가 선언된 곳에서만 해당 빈을 생성 및 주입할 수 있다.
public class DatabaseCleanup implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    /**
     * 이 메소드는 빈이 초기화될 때 호출됩니다. entityManager를 사용하여 데이터베이스의 모든 엔티티를 가져오고, 각 엔티티의 이름을 추출하여 tableNames 리스트에 저장합니다.
     * entityManager.getMetamodel().getEntities()를 사용하여 데이터베이스의 모든 엔티티 메타데이터를 가져옵니다.
     * 각 엔티티의 이름을 가져와서 CamelCase로 변환합니다(CaseFormatUtils.toCamelCase 메서드 사용).
     */
    @Override
    public void afterPropertiesSet() {
        //  이 메서드가 JPA 엔티티의 이름(즉, 클래스 이름)을 반환하기 때문입니다. JPA 엔티티 이름과 실제 데이터베이스 테이블 이름이 다를 수 있기 때문에, 실제 테이블 이름을 가져오기 위해서는 @Table 어노테이션을 확인해야 합니다.
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(entity -> entity.getJavaType().getAnnotation(Entity.class) != null)
                .map(entity -> {
                    // @Table 주석이 있는 경우 테이블 이름을 검색합니다.
                    Table tableAnnotation = entity.getJavaType().getAnnotation(Table.class);
                    if (tableAnnotation != null && !tableAnnotation.name().isEmpty()) {
                        return tableAnnotation.name();
                    } else {
                        return toSnakeCase(entity.getName());
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 이 메소드는 데이터베이스를 초기화하는 작업을 수행합니다. 트랜잭션 내에서 실행되며, 모든 작업이 하나의 트랜잭션으로 묶입니다.
     * entityManager.flush()를 호출하여 쓰기 지연 저장소에 남아 있는 SQL 문을 먼저 수행합니다.
     * 외래 키 제약 조건을 해제(SET REFERENTIAL_INTEGRITY FALSE)하여 테이블을 비울 수 있도록 합니다.
     * tableNames 리스트에 있는 모든 테이블 이름에 대해 TRUNCATE TABLE 명령을 실행하여 테이블을 비웁니다.
     * 각 테이블의 ID 컬럼 값을 1부터 시작하도록 초기화(ALTER TABLE ... ALTER COLUMN ID RESTART WITH 1)합니다.
     * 외래 키 제약 조건을 다시 활성화(SET REFERENTIAL_INTEGRITY TRUE)합니다.
     */
    @Transactional
    public void execute() {
        // 쓰기 지연 저장소에 남은 SQL을 먼저 수행
        entityManager.flush();
        // 연관 관계 맵핑된 테이블이 있는 경우 참조 무결성을 해제해야 TRUNCATE를 할 수 있다.
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

        for (String tableName : tableNames) {
            // 테이블 이름을 순회하면서 TRUNCATE SQL문을 수행
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            // 테이블의 내부 값이 지워지고 그 다음번 넣을 ID값을 다시 1부터 시작할 수 있도록 기본 값 초기화
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1").executeUpdate();
        }
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }

    private static String toSnakeCase(String str) {
//        System.out.println(str);
        String regex = "([a-z])([A-Z])";
        String replacement = "$1_$2";
        return str.replaceAll(regex, replacement).toLowerCase();
    }
}
