package com.example.todo.domain.repository;

import com.example.todo.domain.entity.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @DisplayName("UserRepository 회원 생성 테스트")
    @Test
    void saveUser() {
        // given
        final String username = "아이디";
        User user = User.builder()
                .username(username)
                .build();

        User savedUser = userRepository.save(user);

        // when
        User findUser = userRepository.findById(savedUser.getId()).get();

        // then
        assertThat(findUser.getUsername()).isEqualTo(username);

    }

}