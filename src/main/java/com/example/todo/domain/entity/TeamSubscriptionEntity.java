package com.example.todo.domain.entity;

import com.example.todo.domain.entity.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "team_subscription")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamSubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private SubscriptionStatus subscriptionStatus;
    private BigDecimal subscriptionPrice;
    private String merchantUid;

    @ManyToOne(cascade = CascadeType.ALL)
    private TeamEntity team;

    @ManyToOne(cascade = CascadeType.ALL)
    private SubscriptionEntity subscription;

    @OneToOne(mappedBy = "teamSubscription")
    private TeamActiveSubscriptionEntity teamActiveSubscription;

    @OneToOne(mappedBy = "teamSubscription")
    private PaymentEntity payment;


    public void changeSubscriptionStatus(SubscriptionStatus subscriptionStatus){
        this.subscriptionStatus = subscriptionStatus;
    }

    public void unlinkTeamActiveSubscription(){
        if (teamActiveSubscription != null){
            teamActiveSubscription = null;
        }
    }
    @Builder
    public TeamSubscriptionEntity(Long id, LocalDate startDate, LocalDate endDate, SubscriptionStatus subscriptionStatus, BigDecimal subscriptionPrice, String merchantUid, TeamEntity team, SubscriptionEntity subscription, TeamActiveSubscriptionEntity teamActiveSubscription, PaymentEntity payment) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subscriptionStatus = subscriptionStatus;
        this.subscriptionPrice = subscriptionPrice;
        this.merchantUid = merchantUid;
        this.team = team;
        this.subscription = subscription;
        this.teamActiveSubscription = teamActiveSubscription;
        this.payment = payment;
    }
}
