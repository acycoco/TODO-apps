package com.example.todo.domain.entity;

import com.example.todo.domain.entity.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(cascade = CascadeType.ALL)
    private TeamEntity team;

    @ManyToOne(cascade = CascadeType.ALL)
    private SubscriptionEntity subscription;

    @OneToOne(mappedBy = "teamSubscription")
    private TeamActiveSubscriptionEntity teamActiveSubscription;

    public void changeSubscriptionStatus(SubscriptionStatus subscriptionStatus){
        this.subscriptionStatus = subscriptionStatus;
    }

    public void unlinkTeamActiveSubscription(){
        if (teamActiveSubscription != null){
            teamActiveSubscription.unlinkSubscription();
            teamActiveSubscription = null;
        }
    }
    @Builder
    public TeamSubscriptionEntity(Long id, LocalDate startDate, LocalDate endDate, SubscriptionStatus subscriptionStatus, TeamEntity team, SubscriptionEntity subscription) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subscriptionStatus = subscriptionStatus;
        this.team = team;
        this.subscription = subscription;
    }
}
