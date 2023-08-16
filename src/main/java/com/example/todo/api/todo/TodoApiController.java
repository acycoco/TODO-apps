package com.example.todo.api.todo;

import com.example.todo.dto.ResponseDto;
import com.example.todo.dto.TodoApiDto;
import com.example.todo.service.todo.TodoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/user/{userId}")
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
    @PostMapping("/{todoId}/like")
    public ResponseEntity<String> likeTodo(@PathVariable Long todoId) {
        // TodoService를 이용하여 해당 todoId에 해당하는 todo에 좋아요를 추가하는 로직 호출
        boolean success = service.likeTodoById(todoId);

        if (success) {
            return ResponseEntity.ok("좋아요를 눌렀습니다.");
        } else {
            return ResponseEntity.badRequest().body("좋아요 실패");
        }
    }
    @PostMapping("/{todoId}/unlike")
    public ResponseEntity<String> unlikeTodo(@PathVariable Long todoId) {
        boolean success = service.unlikeTodoById(todoId);

        if (success) {
            return ResponseEntity.ok("좋아요를 취소했습니다.");
        } else {
            return ResponseEntity.badRequest().body("Failed to unlike the todo.");
        }
    }
}
