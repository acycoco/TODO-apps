package com.example.todo.dto;

import com.example.todo.domain.entity.TaskApiEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

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
    private LocalDate startDate;
    @NotNull(message = "마감일을 작성해주세요.")
    private LocalDate dueDate;
    private String status;

    public static TaskApiDto fromEntity(TaskApiEntity entity) {
        TaskApiDto taskApiDto = new TaskApiDto();
        taskApiDto.setId(entity.getId());
        taskApiDto.setTaskName(entity.getTaskName());
        taskApiDto.setTaskDesc(entity.getTaskDesc());
        taskApiDto.setStartDate(entity.getStartDate());
        taskApiDto.setDueDate(entity.getDueDate());
        taskApiDto.setStatus(entity.getStatus());
        return taskApiDto;
    }
}