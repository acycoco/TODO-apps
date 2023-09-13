package com.example.todo.dto.mail;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailDto {

    private String address;
    private String title;
    private String message;

    @Builder
    public MailDto(final String address, final String title, final String message) {
        this.address = address;
        this.title = title;
        this.message = message;
    }
}
