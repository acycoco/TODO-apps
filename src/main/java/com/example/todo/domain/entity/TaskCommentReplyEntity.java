package com.example.todo.domain.entity;

import com.example.todo.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TaskCommentReplyEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User writer;
    private String reply;

    @ManyToOne
    private TaskApiEntity taskApiEntity;

    @ManyToOne
    private TaskCommentEntity taskCommentEntity;
}