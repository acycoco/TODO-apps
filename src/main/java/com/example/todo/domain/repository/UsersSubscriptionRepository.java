package com.example.todo.domain.repository;

import com.example.todo.domain.entity.UsersSubscriptionEntity;
import com.example.todo.domain.entity.enums.SubscriptionStatus;
import com.example.todo.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UsersSubscriptionRepository extends JpaRepository<UsersSubscriptionEntity, Long> {
    List<UsersSubscriptionEntity> findAllByUsers(User user);
    Optional<UsersSubscriptionEntity> findByUsersAndSubscriptionStatus(User user, SubscriptionStatus status);
    Boolean existsByUsersAndSubscriptionStatus(User user, SubscriptionStatus status);
    Optional<UsersSubscriptionEntity> findByMerchantUid(String merchantUid);
    List<UsersSubscriptionEntity> findAllByEndDateBeforeAndSubscriptionStatus(LocalDate localDate, SubscriptionStatus status);
}
