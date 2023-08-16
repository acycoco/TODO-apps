package com.example.todo.dto.user.request;

import com.example.todo.domain.entity.enums.Role;
import com.example.todo.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserJoinRequestDto {

    private String username;
    private String password;
    private String phone;

    @Builder
    public UserJoinRequestDto(final String username, final String password, final String phone) {
        this.username = username;
        this.password = password;
        this.phone = phone;
    }

    public User toEntity(String password) {
        return User.builder()
                .username(username)
                .password(password)
                .phone(phone)
                .role(Role.USER)
                .build();
    }
}
