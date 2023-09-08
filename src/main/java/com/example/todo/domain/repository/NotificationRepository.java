package com.example.todo.domain.repository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Repository
@RequiredArgsConstructor
public class NotificationRepository {
    // 개별 emitters
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    // Emitters 저장
    public void save(Long id, SseEmitter emitter) {
        emitters.put(id, emitter);
    }

    // Emitter 제거
    public void deleteById(Long id) {
        emitters.remove(id);
    }

    // Emitter 가져오기
    public SseEmitter get(Long id) {
        return emitters.get(id);
    }

}