package com.example.todo.api;

import org.springframework.data.jpa.repository.JpaRepository;
    public interface TaskApiRepository extends JpaRepository<TaskApiEntity, Long> {
    }