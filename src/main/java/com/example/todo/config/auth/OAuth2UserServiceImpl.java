package com.example.todo.config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

//    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadUser()");
        // application.yml에 등록한 id가 나온다. registration - kakao, naver, ...
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String nameAttribute = "";

        log.info("registrationId(플랫폼 코드) = {}", registrationId);

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 사용할 데이터를 다시 정리하는 목적의 Map
        Map<String, Object> attributes = new HashMap<>();

        attributes.put("provider", "google");
        attributes.put("id", oAuth2User.getAttribute("sub"));

        Map<String, Object> propMap = oAuth2User.getAttributes();
        attributes.put("username", propMap.get("email"));
        attributes.put("name", propMap.get("name"));
        attributes.put("profileImage", propMap.get("picture"));
        nameAttribute = "username";
        log.info("여기서 끝");
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                attributes,
                nameAttribute
        );
    }

//    private User saveOrUpdate(OAuth2User oAuth2User) {
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//        String username = (String) attributes.get("email");
//        String name = (String) attributes.get("name");
//        String profile = (String) attributes.get("picture");
//
//        log.info("username = {}", username);
//        log.info("name = {}", name);
//
//        User user = userRepository.findByUsername(username)
//                .map(entity -> entity.update(profile))
//                .orElse(User.builder()
//                        .username(username)
//                        .password("OAuth2")
//                        .profileImage(profile)
//                        .role(Role.USER)
//                        .build());
//
//        return userRepository.save(user);
//    }
}
