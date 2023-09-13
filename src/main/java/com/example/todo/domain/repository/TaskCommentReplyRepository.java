package com.example.todo.domain.repository;

import com.example.todo.domain.entity.TaskCommentReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskCommentReplyRepository extends JpaRepository<TaskCommentReplyEntity, Long> {
}