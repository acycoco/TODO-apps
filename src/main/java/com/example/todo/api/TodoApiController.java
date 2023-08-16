package com.example.todo.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoApiController {

    private final TodoApiService service;

    @PostMapping
    public ResponseDto create(@RequestBody TodoApiDto todoApiDto) {
        return service.createTodo(todoApiDto);
    }

    //Todo 상세 조회
    @GetMapping("/{todoId}")
    public TodoApiDto read(@PathVariable("todoId") Long todoId) {
        return service.readTodo(todoId);
    }

    //특정 유저 Todo 목록 조회
    @GetMapping("/{userId}")
    public Page<TodoApiDto> readAll(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
        return service.readUserTodoAll(userId, page, limit);
    }
    //수정
    @PutMapping("/{todoId}")
    public ResponseDto update(
            @PathVariable("todoId") Long todoId,
            @RequestBody TodoApiDto todoApiDto) {
        return service.updateTodo(todoId, todoApiDto);
    }
    @DeleteMapping("/{todoId}")
    public ResponseDto delete(@PathVariable("todoId") Long todoId, @RequestBody TodoApiDto todoApiDto) {
        return service.deleteTodo(todoId, todoApiDto);
    }
}
