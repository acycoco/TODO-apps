package com.example.todo.service.todo;

import com.example.todo.dto.todo.TodoCommentCreateDto;
import com.example.todo.dto.todo.TodoCommentReadDto;
import com.example.todo.dto.todo.TodoCommentUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class TodoCommentService {
    public void createTodoComment(Long userId, Long todoId, TodoCommentCreateDto todoCommentCreateDto) {
    }

    public Page<TodoCommentReadDto> readTodoCommentsPage(Long userId, Long todoId, Integer page, Integer limit) {
        return null;
    }

    public void updateTodoComment(Long userId, Long todoId, Long commentId, TodoCommentUpdateDto todoCommentUpdateDto) {
    }
}
