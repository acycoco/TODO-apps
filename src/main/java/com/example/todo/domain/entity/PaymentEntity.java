package com.example.todo.domain.entity;

import com.example.todo.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "Payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String impUid;

    private BigDecimal amount;

    private String merchantUid;

    private String payStatus;

    private String payMethod;

    private LocalDateTime paymentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private UsersSubscriptionEntity usersSubscription;


    @Builder
    public PaymentEntity(Long id, String impUid, BigDecimal amount, String merchantUid, String payStatus, String payMethod, LocalDateTime paymentDate, User user, UsersSubscriptionEntity usersSubscription) {
        this.id = id;
        this.impUid = impUid;
        this.amount = amount;
        this.merchantUid = merchantUid;
        this.payStatus = payStatus;
        this.payMethod = payMethod;
        this.paymentDate = paymentDate;
        this.user = user;
        this.usersSubscription = usersSubscription;
    }
}
