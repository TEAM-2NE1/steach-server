package com.twentyone.steachserver.auth;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// service 레이어에 권한처리가 있다면 유용함.
// 현재는 필요 없음.
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockTeacherSecurityContextFactory.class)
public @interface WithAuthTeacher {
    String teacherUserName() default "t12345";
    String password() default "password";
    String roles() default "ROLE_TEACHER";
}
