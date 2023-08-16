package com.example.todo.dto.user.response;

import com.example.todo.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserJoinResponseDto {

    private Long id;
    private String username;

    public UserJoinResponseDto(final User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}
