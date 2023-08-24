package com.example.todo.domain.repository;

import com.example.todo.domain.entity.TeamSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamSubscriptionRepository extends JpaRepository<TeamSubscriptionEntity, Long> {
}
