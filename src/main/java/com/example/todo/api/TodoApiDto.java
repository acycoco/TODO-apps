package com.example.todo.api;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TodoApiDto {
    private Long id;
    @NotNull(message = "제목을 작성해주세요.")
    private String title;
    @NotNull(message = "설명을 작성해주세요.")
    private String content;
    @NotNull(message = "시작일을 입력해주세요")
    private String startDate;
    @NotNull(message = "마감일을 작성해주세요.")
    private String dueDate;
    private String status;

    public static TodoApiDto fromEntity(TodoApiEntity entity) {
        TodoApiDto todoApiDto = new TodoApiDto();
        todoApiDto.setId(entity.getId());
        todoApiDto.setTitle(entity.getTitle());
        todoApiDto.setContent(entity.getContent());
        todoApiDto.setStartDate(entity.getStartDate());
        todoApiDto.setDueDate(entity.getDueDate());
        return todoApiDto;
    }
}