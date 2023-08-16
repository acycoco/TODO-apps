package com.example.todo.domain.entity.user;

import com.example.todo.domain.entity.enums.Role;
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

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(final String username, final String password, final String profileImage, final String phone,
                final Role role) {
        this.username = username;
        this.password = password;
        this.profileImage = profileImage;
        this.phone = phone;
        this.role = role;
    }
}
