package com.example.todo.domain.repository;

import com.example.todo.domain.entity.TaskApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskApiRepository extends JpaRepository<TaskApiEntity, Long> {
}