package com.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamCreateDto {
    //Validation 추가 필요
    private String name;
    private String description;
    //Validation 추가 필요
    private String joinCode;
}
