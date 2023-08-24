package com.example.todo.domain.repository;

import com.example.todo.domain.entity.SubscriptionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    Page<SubscriptionEntity> findAllByStatusIsTrue(Pageable pageable);
}
