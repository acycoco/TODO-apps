package com.example.todo.config.auth;

import com.example.todo.domain.entity.enums.Role;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import com.example.todo.jwt.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
// OAuth2 통신이 성공적으로 끝났을 때, 사용하는 클래스
// JWT를 활용한 인증 구현하고 있기 때문에
// ID Provider에게 받은 정보를 바탕으로 JWT를 발급하는 역할을 하는 용도
// JWT를 발급하고 클라이언트가 저장할 수 있도록 특정 URL로 리다이렉트 시키자.
public class OAuth2SuccessHandler
        // 인증 성공 후 특정 URL로 리다이렉트 시키고 싶을 때 활용할 수 있는
        // successHandler
        extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    public OAuth2SuccessHandler(
            TokenProvider tokenProvider,
            UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    // 인증 성공시 호출되는 메소드
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        log.info("onAuthenticationSuccess");
        // OAuth2UserServiceImpl에서 반환한 DefaultOAuth2User가 저장된다.
        OAuth2User oAuth2User
                = (OAuth2User) authentication.getPrincipal();
        // 소셜 로그인을 한 새로운 사용자를 우리의 UserEntity로 전환하기 위한 작업
        // username: Email을 @ 기준으로 나누고,
        //     ID Provider(Naver) 같은 값을 추가하여 조치
        String email = oAuth2User.getAttribute("email");
        String provider = oAuth2User.getAttribute("provider");
        String username = email;
        String providerId = oAuth2User.getAttribute("id").toString();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        // 처음으로 소셜 로그인한 사용자를 데이터베이스에 등록
        if (optionalUser.isEmpty()) {
            User user = new User(username, providerId,null,null, Role.USER);
            userRepository.save(user);
        }

        // 데이터베이스에서 사용자 회수
        User user = userRepository.findByUsername(username).get();

        // JWT 생성
//        String jwt = tokenUtils
//                .generateToken(User
//                        .withUsername(oAuth2User.getName())
//                        .password(oAuth2User.getAttribute("id").toString())
//                        .build());

        String jwt = tokenProvider.createAccessToken(user);
        log.info(jwt);

        // 목적지 URL 설정
        // 우리 서비스의 Frontend 구성에 따라 유연하게 대처해야 한다.
        String targetUrl = String.format(
                "http://localhost:8080/api/val?token=%s", jwt
        );
        // 실제 Redirect 응답 생성
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
