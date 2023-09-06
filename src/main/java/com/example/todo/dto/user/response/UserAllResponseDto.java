package com.example.todo.dto.user.response;

import com.example.todo.domain.entity.UsersSubscriptionEntity;
import com.example.todo.domain.entity.enums.SubscriptionStatus;
import com.example.todo.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserAllResponseDto {

    private Long id;
    private String username;
    private String phone;
    private LocalDate start;
    private LocalDate end;
    private SubscriptionStatus status;

    public UserAllResponseDto(final UsersSubscriptionEntity usersSubscription, final User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.phone = user.getPhone();
        this.start = usersSubscription.getStartDate();
        this.end = usersSubscription.getEndDate();
        this.status = usersSubscription.getSubscriptionStatus();
    }
}
