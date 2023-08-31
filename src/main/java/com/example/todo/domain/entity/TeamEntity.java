package com.example.todo.domain.entity;

import com.example.todo.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String joinCode;
    private Integer participantNum;
    private Integer participantNumMax;

    @ManyToOne
    private User manager;

    @OneToMany
    private List<MemberEntity> member;

}
