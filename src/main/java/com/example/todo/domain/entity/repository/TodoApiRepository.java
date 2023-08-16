package com.example.todo.domain.entity.repository;

import com.example.todo.domain.entity.entity.TodoApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TodoApiRepository extends JpaRepository<TodoApiEntity, Long> {
}