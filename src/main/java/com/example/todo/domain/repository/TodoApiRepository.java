package com.example.todo.domain.repository;

import com.example.todo.domain.entity.TodoApiEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TodoApiRepository extends JpaRepository<TodoApiEntity, Long> {
    Page<TodoApiEntity> findByUserId(Long userId, Pageable pageable);
}