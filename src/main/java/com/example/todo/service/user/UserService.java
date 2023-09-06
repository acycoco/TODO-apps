package com.example.todo.service.user;

import com.example.todo.domain.entity.SubscriptionEntity;
import com.example.todo.domain.entity.UsersSubscriptionEntity;
import com.example.todo.domain.entity.enums.Role;
import com.example.todo.domain.entity.enums.SubscriptionStatus;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.SubscriptionRepository;
import com.example.todo.domain.repository.UsersSubscriptionRepository;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.user.request.UserJoinRequestDto;
import com.example.todo.dto.user.request.UserUpdateRequestDto;
import com.example.todo.dto.user.response.UserJoinResponseDto;
import com.example.todo.dto.user.response.UserUpdateResponseDto;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static com.example.todo.exception.ErrorCode.ALREADY_USER_USERNAME;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SubscriptionRepository subscriptionRepository;
    private final UsersSubscriptionRepository usersSubscriptionRepository;

    @Transactional
    public UserJoinResponseDto createUser(final UserJoinRequestDto joinDto) {
        validateDuplicateUsername(joinDto.getUsername());
        User user = joinDto.toEntity(passwordEncoder.encode(joinDto.getPassword()));
        return new UserJoinResponseDto(userRepository.save(user));
    }

    @Transactional
    public UserUpdateResponseDto updateUser(final UserUpdateRequestDto updateDto, final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        user.updateProfile(updateDto, passwordEncoder.encode(updateDto.getPassword()), "image");
        return new UserUpdateResponseDto(user);
    }

//    @Transactional
//    public void createUserData() {
//        for (int i = 1; i <= 10000; i++) {
//            UserJoinRequestDto joinDto = UserJoinRequestDto.builder()
//                    .password("1234")
//                    .username("user" + i)
//                    .phone("010")
//                    .build();
//            User user = joinDto.toEntity(passwordEncoder.encode(joinDto.getPassword()));
//            userRepository.save(user);
//        }
//    }
//    private String generateMerchantUid(){
//        Instant instant = Instant.now();
//        Long timestamp = instant.toEpochMilli();
//        return "order_" + timestamp;
//    }
//
//    @Transactional
//    public void userSubscription() {
//        SubscriptionEntity subscription = subscriptionRepository.findById(1L)
//                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_SUBSCRIPTION));
//
//        List<User> all = userRepository.findAll();
//        System.out.println("all.size() = " + all.size());
//        for (int i = 1; i <= 10000; i++) {
//            int days = 30;
//            if (i <= 5000) {
//                days = 30;
//            } else {
//                days = 7;
//            }
//            UsersSubscriptionEntity usersSubscription = UsersSubscriptionEntity.builder()
//                    .users(all.get(i))
//                    .subscription(subscription)
//                    .startDate(LocalDate.now())
//                    .endDate(LocalDate.now().plusDays(days))
//                    .subscriptionStatus(SubscriptionStatus.ACTIVE)
//                    .merchantUid(generateMerchantUid())
//                    .subscriptionPrice(subscription.getPrice())
//                    .build();
//
//            usersSubscriptionRepository.save(usersSubscription);
//        }
//    }

    @Transactional
    public void createAdminUser(){
        if (!userRepository.existsByUsername("admin")){
            User user = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(user);
        }
    }

    private void validateDuplicateUsername(String username) {
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new TodoAppException(ALREADY_USER_USERNAME, ALREADY_USER_USERNAME.getMessage());
                });
    }
}
