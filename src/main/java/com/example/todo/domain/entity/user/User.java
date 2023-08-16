package com.example.todo.domain.entity.user;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String profileImage;
    private String phone;

    @Builder
    public User(final String username, final String password, final String profileImage, final String phone) {
        this.username = username;
        this.password = password;
        this.profileImage = profileImage;
        this.phone = phone;
    }
}
