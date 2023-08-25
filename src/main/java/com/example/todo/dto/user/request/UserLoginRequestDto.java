package com.example.todo.dto.user.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserLoginRequestDto {

    private String username;
    private String password;

    @Builder
    public UserLoginRequestDto(final String username, final String password) {
        this.username = username;
        this.password = password;
    }
}
