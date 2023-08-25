package com.example.todo.api.task;

import com.example.todo.dto.ResponseDto;
import com.example.todo.dto.task.TaskApiDto;
import com.example.todo.service.task.TaskApiService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team/{teamId}/tasks")
public class TaskApiController {

    private final TaskApiService service;

    @PostMapping
    public ResponseDto create(
            @PathVariable("teamId") Long teamId,
            @RequestBody TaskApiDto taskApiDto,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return service.createTask(userId, teamId, taskApiDto);
    }


    @GetMapping("/{taskId}")
    public TaskApiDto read(
            @PathVariable("teamId") Long teamId,
            @PathVariable("taskId") Long taskId) {
        return service.readTask(teamId, taskId);
    }

    @GetMapping
    public Page<TaskApiDto> readAll(
            @PathVariable("teamId") Long teamId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
        return service.readTasksAll(teamId, page, limit);
    }

    //수정
    @PutMapping("/{taskId}")
    public ResponseDto update(
            @PathVariable("teamId") Long teamId,
            @PathVariable("taskId") Long taskId,
            @RequestBody TaskApiDto taskApiDto) {
        return service.updateTask(teamId, taskId, taskApiDto);
    }

    //삭제
    @DeleteMapping("/{taskId}")
    public ResponseDto delete(
            @PathVariable("teamId") Long itemId,
            @PathVariable("taskId") Long taskId,
            Authentication authentication) {
        return service.deleteTask(itemId, taskId, authentication);
    }
}