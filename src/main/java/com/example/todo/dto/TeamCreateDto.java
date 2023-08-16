package com.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TeamCreateDto {
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private String joinCode;
}
