package com.example.todo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    // STOMP 엔드포인트 설정용 메소드
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chatting");
        log.info("Look WebSocketStompConfig");
    }

    @Override
    // MessageBroker를 활용하는 방법 설정
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        log.info("Look WebSocketStompConfig");
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app", "/topic");
    }
}
