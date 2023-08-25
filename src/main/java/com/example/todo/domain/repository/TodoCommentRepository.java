package com.example.todo.domain.repository;

import com.example.todo.domain.entity.TodoApiEntity;
import com.example.todo.domain.entity.TodoCommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoCommentRepository extends JpaRepository<TodoCommentEntity, Long> {
    Page<TodoCommentEntity> findAllByTodoApiEntity(TodoApiEntity todoApiEntity, Pageable pageable);
}
