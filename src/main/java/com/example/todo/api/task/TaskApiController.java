package com.example.todo.api.task;

import com.example.todo.dto.ResponseDto;
import com.example.todo.dto.task.TaskApiDto;
import com.example.todo.service.task.TaskApiService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<TaskApiDto> readAll(
            Authentication authentication,
            @PathVariable("teamId") Long teamId) {
        return service.readTasksAll(teamId);
    }

    //수정
    @PutMapping("/{taskId}")
    public ResponseDto update(
            @PathVariable("teamId") Long teamId,
            @PathVariable("taskId") Long taskId,
            @RequestBody TaskApiDto taskApiDto,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return service.updateTask(userId, teamId, taskId, taskApiDto);
    }

    //삭제
    @DeleteMapping("/{taskId}")
    public ResponseDto delete(
            @PathVariable("teamId") Long itemId,
            @PathVariable("taskId") Long taskId,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return service.deleteTask(userId, itemId, taskId);
    }
}