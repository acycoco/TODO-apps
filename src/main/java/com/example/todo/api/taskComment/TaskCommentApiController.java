package com.example.todo.api.taskComment;

import com.example.todo.dto.ResponseDto;
import com.example.todo.dto.taskComment.TaskCommentCreateDto;
import com.example.todo.dto.taskComment.TaskCommentReadDto;
import com.example.todo.dto.taskComment.TaskCommentUpdateDto;
import com.example.todo.service.taskComment.TaskCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/task/{taskId}/comments")
@RequiredArgsConstructor
public class TaskCommentApiController {
    private final TaskCommentService taskCommentService;
    @PostMapping
    public ResponseDto createTaskComment(Authentication authentication,
                                         @PathVariable("taskId") Long taskId,
                                         @RequestBody TaskCommentCreateDto taskCommentCreateDto) {
        Long userId = Long.parseLong(authentication.getName());

        taskCommentService.createTaskComment(userId, taskId, taskCommentService);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("Task에 댓글이 등록되었습니다.");
        return responseDto;
    }

    @GetMapping
    public Page<TaskCommentReadDto> readTaskCommentReadDtoPage(Authentication authentication,
                                                               @PathVariable("taskId") Long taskId) {
        Long userId = Long.parseLong(authentication.getName());
        Page<TaskCommentReadDto> page = taskCommentService.readTaskCommentsPage(userId, taskId);
        return page;
    }

    @PutMapping("/{commentId}")
    public ResponseDto updateTaskComment(Authentication authentication,
                                         @PathVariable("taskId") Long taskId,
                                         @PathVariable("commentId") Long commentId,
                                         TaskCommentUpdateDto taskCommentUpdateDto) {
        Long userId = Long.parseLong(authentication.getName());
        taskCommentService.updateTaskComment(userId, taskId, commentId, taskCommentUpdateDto);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("Task에 댓글이 수정되었습니다.");
        return responseDto;
    }



}
