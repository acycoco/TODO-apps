package com.example.todo.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Builder
@Getter
public class ErrorResponse {
    private HttpStatus errorCode;
    private String message;
}