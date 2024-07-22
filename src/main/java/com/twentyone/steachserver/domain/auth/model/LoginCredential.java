package com.twentyone.steachserver.domain.auth.model;

import com.twentyone.steachserver.config.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @DiscriminatorColumn 어노테이션은 상속된 엔티티 타입을 구분하는 컬럼을 지정합니다. 이 컬럼은 엔티티 타입을 식별하기 위한 추가 컬럼으로, @Inheritance(strategy = InheritanceType.SINGLE_TABLE) 또는 @Inheritance(strategy = InheritanceType.JOINED) 전략에서 주로 사용됩니다.
 *
 * name: 데이터베이스 테이블에 생성될 컬럼의 이름을 지정합니다. 여기서는 "type"이라고 지정하였습니다.
 * discriminatorType: 구분 컬럼의 데이터 타입을 지정합니다. 기본값은 DiscriminatorType.STRING이며, 문자열 타입으로 저장됩니다.
 * length: 구분 컬럼의 길이를 지정합니다. 기본값은 31입니다.
 */
@NoArgsConstructor
@Entity
@Getter
@Setter(value = AccessLevel.PROTECTED)
@Table(name = "login_credentials")
//@DiscriminatorColumn(name = "type") // 엔티티 타입을 식별하기 위한 컬럼을 추가합니다.
@Inheritance(strategy = InheritanceType.JOINED) // 상속 전략 설정
public class LoginCredential extends BaseTimeEntity implements UserDetails {
    private static final Logger log = LoggerFactory.getLogger(LoginCredential.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 16, nullable = false, unique = true)
    private String username;

    @Column(length = 255, nullable = false)
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
