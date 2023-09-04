package com.example.todo.dto.team;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public class TeamCreateDto {
    //Validation 추가 필요
    private String name;
    private String description;
    //Validation 추가 필요
    private String joinCode;
    private Integer participantNum;

    @Builder
    public TeamCreateDto(final String name, final String description, final String joinCode, final Integer participantNum) {
        this.name = name;
        this.description = description;
        this.joinCode = joinCode;
        this.participantNum = participantNum;
    }
}
