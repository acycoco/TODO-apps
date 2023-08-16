package com.example.todo.config.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse<T> {

    private int code;
    private T data;

    public static <T> TokenResponse<T> authResponse(int code, T data) {
        return new TokenResponse<>(code, data);
    }
}

