package com.example.todo.domain.repository;

import com.example.todo.domain.entity.TeamActiveSubscriptionEntity;
import com.example.todo.domain.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamActiveSubscriptionRepository extends JpaRepository<TeamActiveSubscriptionEntity, Long> {
    Optional<TeamActiveSubscriptionEntity> findByTeam(TeamEntity team);
}
