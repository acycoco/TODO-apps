package com.example.todo.api;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "task")
public class TaskApiEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long teamId;
    private Long userId;
    private String taskName;
    private String taskDesc;
    private String startDate;
    private String dueDate;
    private String status;
}