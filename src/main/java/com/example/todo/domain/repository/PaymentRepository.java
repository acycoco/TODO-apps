package com.example.todo.domain.repository;

import com.example.todo.domain.entity.PaymentEntity;
import com.example.todo.domain.entity.UsersSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByUsersSubscription(UsersSubscriptionEntity usersSubscription);
}
