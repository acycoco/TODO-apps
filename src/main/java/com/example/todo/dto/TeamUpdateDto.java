package com.example.todo.dto;

import com.example.todo.domain.entity.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TeamUpdateDto {
    private String name;
    private String description;
    private String joinCode;
    private User manager;


}
