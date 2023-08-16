package com.example.todo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TodoAppExceptionHandler {
    @ExceptionHandler(TodoAppException.class)
    public ResponseEntity<ErrorResponse> handleTodoException(TodoAppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(errorCode.getHttpStatus())
                .message(errorCode.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }
}
