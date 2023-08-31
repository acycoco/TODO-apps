package com.example.todo.domain.entity;

import com.example.todo.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TodoCommentEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private TodoApiEntity todoApiEntity;
    private String content;

    @ManyToOne
    private User writer;
}
