package com.example.todo.service.user;

import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.user.request.UserJoinRequestDto;
import com.example.todo.dto.user.request.UserUpdateRequestDto;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @DisplayName("회원가입 서비스 테스트")
    @Test
    void saveUser() {
        // given
        final String username = "아이디";
        final String password = "비밀번호";
        UserJoinRequestDto joinDto = UserJoinRequestDto.builder()
                .username(username)
                .password(password)
                .build();

        userService.createUser(joinDto);

        // when
        List<User> all = userRepository.findAll();

        // then
        assertThat(all.get(0).getUsername()).isEqualTo(username);

    }
    
    @DisplayName("회원 정보 수정 서비스 테스트 (아직 이미지 구현X)")
    @Test
    void updateUser() {
        // given
        final String username = "아이디";
        final String password = "비밀번호";
        User user = createUser(username, password);

        final String updatePassword = "변경";
        UserUpdateRequestDto updateDto = UserUpdateRequestDto.builder()
                .password(updatePassword)
                .phone("010-1234-5678")
                .build();

        // when
        userService.updateUser(updateDto, user.getId());
        User findUser = userRepository.findAll().get(0);

        // then
        assertThat(findUser.getPassword()).isNotEqualTo(password);
        
    }

    private User createUser(final String username, final String password) {
        User user = User.builder()
                .username(username)
                .password(password)
                .build();
        return userRepository.save(user);
    }
}