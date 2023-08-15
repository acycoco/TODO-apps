package com.example.todo.api;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskApiDto {
    private Long id;
    private Long teamId;
    private Long userId;
    @NotNull(message = "제목을 작성해주세요.")
    private String taskName;
    @NotNull(message = "설명을 작성해주세요.")
    private String taskDesc;
    @NotNull(message = "시작일을 입력해주세요")
    private String startDate;
    @NotNull(message = "마감일을 작성해주세요.")
    private String dueDate;
    private String status;

    public static TaskApiDto fromEntity(TaskApiEntity entity) {
        TaskApiDto taskApiDto = new TaskApiDto();
        taskApiDto.setId(entity.getId());
        taskApiDto.setTeamId(entity.getTeamId());
        taskApiDto.setTaskName(entity.getTaskName());
        taskApiDto.setTaskDesc(entity.getTaskDesc());
        taskApiDto.setStartDate(entity.getStartDate());
        taskApiDto.setDueDate(entity.getDueDate());
        return taskApiDto;
    }
}