package com.example.todo.domain.repository;

import com.example.todo.domain.entity.TeamSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamSubscriptionRepository extends JpaRepository<TeamSubscriptionEntity, Long> {
    Optional<TeamSubscriptionEntity> findByMerchantUid(String merchantUid);
}
