package com.example.todo.domain.repository;

import com.example.todo.domain.entity.TeamSubscriptionEntity;
import com.example.todo.domain.entity.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TeamSubscriptionRepository extends JpaRepository<TeamSubscriptionEntity, Long> {
    Optional<TeamSubscriptionEntity> findByMerchantUid(String merchantUid);
    List<TeamSubscriptionEntity> findAllByEndDateBeforeAndSubscriptionStatus(LocalDate localDate, SubscriptionStatus status);
}
