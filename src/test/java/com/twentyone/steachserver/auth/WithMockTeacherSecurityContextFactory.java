package com.twentyone.steachserver.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

public class WithMockTeacherSecurityContextFactory implements WithSecurityContextFactory<WithAuthTeacher> {

    @Override
    public SecurityContext createSecurityContext(WithAuthTeacher withAuthTeacher) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserDetails userDetails = User.builder()
                .username(withAuthTeacher.teacherUserName())
                .password(withAuthTeacher.password())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(withAuthTeacher.roles())))
                .build();

        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, "password", userDetails.getAuthorities()));
        return context;
    }
}
