package com.example.todo.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team_active_subscription")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamActiveSubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "activeSubscription")
    private TeamEntity team;

    @OneToOne
    private TeamSubscriptionEntity teamSubscription;

    @Builder
    public TeamActiveSubscriptionEntity(Long id, TeamEntity team, TeamSubscriptionEntity teamSubscription) {
        this.id = id;
        this.team = team;
        this.teamSubscription = teamSubscription;
    }
}
