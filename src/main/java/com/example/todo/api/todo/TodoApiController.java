package com.example.todo.api.todo;

import com.example.todo.dto.ResponseDto;
import com.example.todo.dto.todo.TodoApiDto;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import com.example.todo.service.todo.TodoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoApiController {

    private final TodoApiService service;

    @PostMapping
    public ResponseDto create(
            @RequestBody TodoApiDto todoApiDto,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return service.createTodo(userId,todoApiDto);
    }

    //Todo 상세 조회
    @GetMapping("/{todoId}")
    public TodoApiDto read(@PathVariable("todoId") Long todoId) {
        return service.readTodo(todoId);
    }

    //특정 유저 Todo 목록 조회
    @GetMapping("/users/{userId}")
    public Page<TodoApiDto> readAll(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
        return service.readUserTodoAll(userId, page, limit);
    }
    //Todo 수정
    @PutMapping("/{todoId}")
    public ResponseDto update(
            @PathVariable("todoId") Long todoId,
            @RequestBody TodoApiDto todoApiDto,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return service.updateTodo(userId, todoId, todoApiDto);
    }
    //Todo 삭제
    @DeleteMapping("/{todoId}")
    public ResponseDto delete(
            @PathVariable("todoId") Long todoId,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return service.deleteTodo(userId, todoId);
    }
    //Todo 좋아요 추가, 취소 기능
    @PostMapping("/{todoId}/like")
    public ResponseEntity<ResponseDto> toggleLikeTodo(@PathVariable Long todoId) {
        try {
            ResponseDto responseDto = service.toggleLikeTodoById(todoId);
            return ResponseEntity.ok(responseDto);
        } catch (TodoAppException e) {
            throw new TodoAppException(ErrorCode.NOT_FOUND_TODO);
        }
    }
}

