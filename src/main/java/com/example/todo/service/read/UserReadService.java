package com.example.todo.service.read;

import com.example.todo.domain.entity.UsersSubscriptionEntity;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.UsersSubscriptionRepository;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.user.response.UserAllResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserReadService {

    private final UsersSubscriptionRepository usersSubscriptionRepository;
    private final UserRepository userRepository;

    public List<UserAllResponseDto> getUsersWithExpirationOneWeek() {
        LocalDate localDate = LocalDate.of(2023, 9, 12);
        List<UsersSubscriptionEntity> all = usersSubscriptionRepository.customFindAll(localDate);
        List<UserAllResponseDto> collect = all.stream()
                .map(u -> new UserAllResponseDto(u, u.getUsers()))
                .collect(Collectors.toList());
        return collect;
    }
}
