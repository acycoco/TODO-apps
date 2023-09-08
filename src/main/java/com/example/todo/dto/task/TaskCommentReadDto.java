package com.example.todo.dto.task;

import com.example.todo.domain.entity.TaskCommentEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCommentReadDto {
    private String writerName;
    private String content;
    private String reply;

    public static TaskCommentReadDto fromEntity(TaskCommentEntity entity) {
        TaskCommentReadDto taskCommentReadDto = new TaskCommentReadDto();
        taskCommentReadDto.setWriterName(entity.getWriter().getUsername());
        taskCommentReadDto.setContent(entity.getContent());
        taskCommentReadDto.setReply(entity.getReply());
        return taskCommentReadDto;
    }
}
