package com.example.todo.api;

import org.springframework.data.jpa.repository.JpaRepository;
public interface TodoApiRepository extends JpaRepository<TodoApiEntity, Long> {
}