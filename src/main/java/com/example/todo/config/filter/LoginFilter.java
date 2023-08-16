package com.example.todo.config.filter;

import com.example.todo.domain.entity.user.PrincipalUserDetails;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.token.RefreshTokenRepository;
import com.example.todo.jwt.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User user = objectMapper.readValue(request.getInputStream(), User.class);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            log.error("{}", e);
        }
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalUserDetails userDetails = (PrincipalUserDetails) authResult.getPrincipal();
        String token = tokenProvider.createAccessToken(userDetails.getUser());
        String refreshToken = UUID.randomUUID().toString();

        setTokenResponse(response, token, refreshToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // Post.permitAll 하면 정상적으로 401 반환
        // 임시로 401 반환하게 설정해놓음
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> result = new HashMap<>();
        result.put("error", "Authentication failed");
        response.getWriter().println(objectMapper.writeValueAsString(result));
//        super.unsuccessfulAuthentication(request, response, failed);
    }

    private void setTokenResponse(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);

        response.getWriter().println(
                objectMapper.writeValueAsString(
//                        Response.success(result)));
                TokenResponse.authResponse(response.getStatus(), result)));
    }
}