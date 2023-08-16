package com.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TeamJoinDto {
    @NotBlank
    private String joinCode;
}
