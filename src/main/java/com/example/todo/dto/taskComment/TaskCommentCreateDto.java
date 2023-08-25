package com.example.todo.dto.taskComment;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SecondaryRow;

@Getter
@Setter
public class TaskCommentCreateDto {
    private String content;
}
