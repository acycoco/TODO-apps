package com.example.todo.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NotificationEntity {
    private String title;
    private String content;
}