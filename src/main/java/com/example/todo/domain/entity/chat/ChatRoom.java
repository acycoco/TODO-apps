package com.example.todo.domain.entity.chat;

import com.example.todo.domain.entity.BaseTimeEntity;
import com.example.todo.domain.entity.task.TaskApiEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Setter
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private TaskApiEntity taskApiEntity;

}
