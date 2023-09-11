package com.example.todo.dto.task;

import lombok.Data;

@Data
public class TaskAndTeamDto {
    private Long teamId;
    private String teamName;
    private Long taskId;
    private String taskName;
    private String taskDesc;
    private String status;
}