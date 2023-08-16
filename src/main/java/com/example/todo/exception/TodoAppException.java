package com.example.todo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TodoAppException extends RuntimeException {
    private final ErrorCode errorCode;
    public TodoAppException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
