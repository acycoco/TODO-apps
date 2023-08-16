package com.example.todo.dto.user.response;

import com.example.todo.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserUpdateResponseDto {

    private Long id;
    private String username;
    private String phone;
    private String profileImage;

    public UserUpdateResponseDto(final User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.phone = user.getPhone();
        this.profileImage = user.getProfileImage();
    }
}
