package com.example.todo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    /* Common Error */
    INVALID_INPUT_VALUE(BAD_REQUEST, " 잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다."),

    NOT_MATCH_USERID(NOT_FOUND, "담당자가 아닙니다."),
    NOT_MATCH_TEAM_AND_TASK(NOT_FOUND, "해당팀의 업무가 아닙니다."),

    NOT_FOUND_ENTITY(NOT_FOUND, "데이터가 존재하지 않습니다."),
    NOT_FOUND_TEAM(NOT_FOUND, "해당팀이 존재하지 않습니다."),
    NOT_FOUND_TODO(NOT_FOUND, "해당TODO가 존재하지 않습니다."),
    NOT_FOUND_USER(NOT_FOUND, "해당USER가 존재하지 않습니다."),
    NOT_FOUND_TASK(NOT_FOUND, "해당업무가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}