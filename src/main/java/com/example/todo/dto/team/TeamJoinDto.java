package com.example.todo.dto.team;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamJoinDto {
    @NotBlank
    private String joinCode;

    @Builder
    public TeamJoinDto(final String joinCode) {
        this.joinCode = joinCode;
    }
}
