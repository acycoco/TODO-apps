package com.example.todo.domain.repository;

import com.example.todo.domain.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByUserIdAndTodoId(Long userId, Long todoId);
}
