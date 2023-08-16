package com.example.todo.api;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "todo")
public class TodoApiEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String startDate;
    private String dueDate;
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