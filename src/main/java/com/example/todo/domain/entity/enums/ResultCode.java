package com.example.todo.domain.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ResultCode {

    SUCCESS(HttpStatus.OK);

    private final HttpStatus httpStatus;
}
