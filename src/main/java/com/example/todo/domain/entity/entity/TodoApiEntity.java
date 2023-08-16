package com.example.todo.domain.entity.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "todo")
public class TodoApiEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate dueDate;
    private String status;
    private int likes = 0;
    public void addLike() {
        this.likes++;
    }
    public void removeLike() {
        if (this.likes > 0) {
            this.likes--;
        }
    }
}