package com.example.todo.api.todo;

import com.example.todo.dto.ResponseDto;
import com.example.todo.dto.todo.TodoCommentCreateDto;
import com.example.todo.dto.todo.TodoCommentReadDto;
import com.example.todo.dto.todo.TodoCommentUpdateDto;
import com.example.todo.service.todo.TodoCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/todo/{todoId}/comments")
@RequiredArgsConstructor
public class TodoCommentApiController {
    private final TodoCommentService todoCommentService;

    @PostMapping
    public ResponseDto createTodoComment(Authentication authentication,
                                         @PathVariable("todoId") Long todoId,
                                         @RequestBody TodoCommentCreateDto todoCommentCreateDto) {
        Long userId = Long.parseLong(authentication.getName());

        todoCommentService.createTodoComment(userId, todoId, todoCommentCreateDto);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("Todo에 댓글이 등록되었습니다.");
        return responseDto;
    }

    @GetMapping
    public Page<TodoCommentReadDto> readTaskCommentReadDtoPage(Authentication authentication,
                                                               @PathVariable("todoId") Long todoId,
                                                               @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                               @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        Long userId = Long.parseLong(authentication.getName());
        Page<TodoCommentReadDto> todoCommentReadDtoPage = todoCommentService.readTodoCommentsPage(userId, todoId, page, limit);
        return todoCommentReadDtoPage;
    }

    @PutMapping("/{commentId}")
    public ResponseDto updateTodoComment(Authentication authentication,
                                         @PathVariable("todoId") Long todoId,
                                         @PathVariable("commentId") Long commentId,
                                         TodoCommentUpdateDto todoCommentUpdateDto) {
        Long userId = Long.parseLong(authentication.getName());
        todoCommentService.updateTodoComment(userId, todoId, commentId, todoCommentUpdateDto);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("Todo에 댓글이 수정되었습니다.");
        return responseDto;
    }
}
