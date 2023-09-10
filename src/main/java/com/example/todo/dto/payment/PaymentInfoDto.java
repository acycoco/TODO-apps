package com.example.todo.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
public class PaymentInfoDto {
    private String impUid; // 아임포트 고유 결제 번호
    private String merchantUid; // 가맹점 주문 번호
    private String name;
    private String paidAt;
    private BigDecimal paidAmount;
    private String payMethod;
    private String payStatus;

    public LocalDateTime convertFromUnixTimestamp(String paidAt) {
        long timestamp = Long.parseLong(paidAt);
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }
}
