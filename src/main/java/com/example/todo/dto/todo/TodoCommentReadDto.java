package com.example.todo.dto.todo;

import com.example.todo.domain.entity.TaskCommentEntity;
import com.example.todo.domain.entity.TodoCommentEntity;
import com.example.todo.dto.task.TaskCommentReadDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoCommentReadDto {
    private String writerName;
    private String content;

    public static TodoCommentReadDto fromEntity(TodoCommentEntity entity) {
        TodoCommentReadDto todoCommentReadDto = new TodoCommentReadDto();
        todoCommentReadDto.setWriterName(entity.getWriter().getUsername());
        todoCommentReadDto.setContent(entity.getContent());
        return todoCommentReadDto;
    }
}
