package com.example.todo.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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
    private BigDecimal price;
    private String description;
    private Boolean status;

    @OneToMany(mappedBy = "subscription")
    private List<UsersSubscriptionEntity> userSubscriptions;

    public void changeName(String name){
        this.name = name;
    }

    public void changeMaxMember(Integer maxMember){
        this.maxMember = maxMember;
    }


    public void changePrice(BigDecimal price){
        this.price = price;
    }

    public void changeDescription(String description){
        this.description = description;
    }

    public void changeStatus(Boolean status){
        this.status = status;
    }

    @Builder
    public SubscriptionEntity(Long id, String name, Integer maxMember, BigDecimal price, String description, Boolean status) {
        this.id = id;
        this.name = name;
        this.maxMember = maxMember;
        this.price = price;
        this.description = description;
        this.status = status;
    }
}
