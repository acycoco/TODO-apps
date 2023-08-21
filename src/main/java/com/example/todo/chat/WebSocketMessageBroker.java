package com.example.todo.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBroker implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/chatting");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) { // 목적지와 상세 엔드포인트를 설정
        registry.enableSimpleBroker("/{teamId}/task/{taskId}"); // 명세서에 추가 및 수정필요
        registry.setApplicationDestinationPrefixes("/chat"); // 명세서에 추가 및 수정필요
    }

}
