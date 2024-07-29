package com.twentyone.steachserver.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.List;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthTeacher> {

    @Override
    public SecurityContext createSecurityContext(WithAuthTeacher withUser) {
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        UserDetails principal = new User(
                withUser.teacherUserName(),
                withUser.password(),
                true,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority(withUser.roles()))
        );

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(withUser.roles()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        securityContext.setAuthentication(authentication);

        return securityContext;
    }
}
