package com.example.todo.dto;

import com.example.todo.domain.entity.TodoApiEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TodoApiDto {
    private Long id;
    @NotNull(message = "제목을 작성해주세요.")
    private String title;
    @NotNull(message = "설명을 작성해주세요.")
    private String content;
    @NotNull(message = "시작일을 입력해주세요")
    private LocalDate startDate;
    @NotNull(message = "마감일을 작성해주세요.")
    private LocalDate dueDate;
    private String status;
    private int likes;

    public static TodoApiDto fromEntity(TodoApiEntity entity) {
        TodoApiDto todoApiDto = new TodoApiDto();
        todoApiDto.setId(entity.getId());
        todoApiDto.setTitle(entity.getTitle());
        todoApiDto.setContent(entity.getContent());
        todoApiDto.setStartDate(entity.getStartDate());
        todoApiDto.setDueDate(entity.getDueDate());
        todoApiDto.setLikes(entity.getLikes()); // 추가: 좋아요 개수 설정
        todoApiDto.setStatus(entity.getStatus());
        return todoApiDto;
    }
}