package com.example.todo.domain.entity;

import com.example.todo.domain.entity.enums.SubscriptionStatus;
import com.example.todo.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "users_subscription")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsersSubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;
    private BigDecimal subscriptionPrice;
    private String merchantUid;

    @ManyToOne(fetch = FetchType.LAZY)
    private User users;

    @ManyToOne(fetch = FetchType.LAZY)
    private SubscriptionEntity subscription;


    @OneToOne(fetch = FetchType.LAZY, mappedBy = "usersSubscription")
    private PaymentEntity payment;


    public void changeSubscriptionStatus(SubscriptionStatus subscriptionStatus){
        this.subscriptionStatus = subscriptionStatus;
    }


    @Builder
    public UsersSubscriptionEntity(Long id, LocalDate startDate, LocalDate endDate, SubscriptionStatus subscriptionStatus, BigDecimal subscriptionPrice, String merchantUid, User users, SubscriptionEntity subscription, PaymentEntity payment) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subscriptionStatus = subscriptionStatus;
        this.subscriptionPrice = subscriptionPrice;
        this.merchantUid = merchantUid;
        this.users = users;
        this.subscription = subscription;
//        this.payment = payment;
    }
}
