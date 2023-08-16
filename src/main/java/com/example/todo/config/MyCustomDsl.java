package com.example.todo.config;

import com.example.todo.config.filter.JwtFilter;
import com.example.todo.config.filter.LoginFilter;
import com.example.todo.domain.repository.token.RefreshTokenRepository;
import com.example.todo.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http
                .addFilter(new LoginFilter(authenticationManager, tokenProvider, refreshTokenRepository))
                .addFilterAfter(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
