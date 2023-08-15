package com.example.todo.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TeamJoinDto {
    @NotBlank
    private String joinCode;
}
