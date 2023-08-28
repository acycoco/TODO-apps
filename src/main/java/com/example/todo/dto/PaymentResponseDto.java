package com.example.todo.dto;

import com.example.todo.domain.entity.PaymentEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentResponseDto {
    private Long id;
    private String impUid;
    private BigDecimal amount;
    private String merchantUid;
    private String payStatus;
    private String payMethod;
    private LocalDateTime paymentDate;
    private Long userId;
    private Long teamSubscriptionId;

    public static PaymentResponseDto fromEntity(PaymentEntity entity){
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(entity.getId());
        dto.setImpUid(entity.getImpUid());
        dto.setAmount(entity.getAmount());
        dto.setMerchantUid(entity.getMerchantUid());
        dto.setPayStatus(entity.getPayStatus());
        dto.setPayMethod(entity.getPayMethod());
        dto.setPaymentDate(entity.getPaymentDate());
        dto.setUserId(entity.getUser().getId());
        dto.setTeamSubscriptionId(entity.getTeamSubscription().getId());
        return dto;
    }
}
