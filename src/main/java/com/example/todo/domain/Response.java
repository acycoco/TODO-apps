package com.example.todo.domain;

import com.example.todo.domain.entity.enums.ResultCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class Response<T> {

    private final ResultCode resultCode;
    private final T date;

    public Response(final ResultCode resultCode, final T date) {
        this.resultCode = resultCode;
        this.date = date;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(ResultCode.SUCCESS, data);
    }
}
