package com.example.todo.domain.entity;

import com.example.todo.domain.entity.MemberEntity;
import com.example.todo.domain.entity.TeamEntity;
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
    private Long userId;
    private String taskName;
    private String taskDesc;
    private LocalDate startDate;
    private LocalDate dueDate;
    private String status;
    @ManyToOne
    private MemberEntity member;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity team;

    public Long getWorkerId() {
        return member.getUser().getId();
    }
}