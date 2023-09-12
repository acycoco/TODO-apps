package com.example.todo.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public Boolean lock(final Long key) {
        return redisTemplate
                .opsForValue()
                .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3000)); //lock 자동 해제 시간 3000ms
    }

    public Boolean unlock(final Long key){
        return redisTemplate.delete(generateKey(key));
    }

    private String generateKey(final Long key) {
        return key.toString();
    }
}
