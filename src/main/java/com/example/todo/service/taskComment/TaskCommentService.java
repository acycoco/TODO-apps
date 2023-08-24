package com.example.todo.service.taskComment;

import com.example.todo.dto.taskComment.TaskCommentReadDto;
import com.example.todo.dto.taskComment.TaskCommentUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class TaskCommentService {
    public void createTaskComment(Long userId, Long taskId, TaskCommentService taskCommentService) {
    }

    public Page<TaskCommentReadDto> readTaskCommentsPage(Long userId, Long taskId) {
        return null;
    }

    public void updateTaskComment(Long userId, Long taskId, Long commentId, TaskCommentUpdateDto taskCommentUpdateDto) {
    }
}
