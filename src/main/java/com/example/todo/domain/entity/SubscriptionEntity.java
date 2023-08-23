package com.example.todo.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscription")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer maxMember;
    private Integer price;
    private String description;

    @Builder
    public SubscriptionEntity(Long id, String name, Integer maxMember, Integer price, String description) {
        this.id = id;
        this.name = name;
        this.maxMember = maxMember;
        this.price = price;
        this.description = description;
    }
}
