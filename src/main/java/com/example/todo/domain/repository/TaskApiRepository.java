package com.example.todo.domain.repository;

import com.example.todo.api.TaskApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
    public interface TaskApiRepository extends JpaRepository<TaskApiEntity, Long> {
    }