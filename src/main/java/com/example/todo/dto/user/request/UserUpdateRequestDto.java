package com.example.todo.dto.user.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserUpdateRequestDto {

    private String password;
    private String phone;
    private String profileImage;

    @Builder
    public UserUpdateRequestDto(final String password, final String phone, final String profileImage) {
        this.password = password;
        this.phone = phone;
        this.profileImage = profileImage;
    }
}
