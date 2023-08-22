package com.example.todo.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notification")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdTime;

    @OneToOne
    private TaskApiEntity taskApiEntity;
    @ManyToOne
    private MemberEntity member ;
}