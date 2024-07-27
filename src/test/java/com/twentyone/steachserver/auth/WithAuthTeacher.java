package com.twentyone.steachserver.auth;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithAuthTeacher {
    String teacherUserName() default "t12345";
    String email() default "t12345@gmail.com";
    String password() default "password";
    String roles() default "ROLE_TEACHER";

}
