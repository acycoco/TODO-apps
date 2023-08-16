package com.example.todo.domain.entity.task;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

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
    private LocalDate startDate;
    private LocalDate dueDate;
    private String status;
}