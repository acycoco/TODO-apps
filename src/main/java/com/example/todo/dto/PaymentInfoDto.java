package com.example.todo.dto;

import com.example.todo.domain.entity.enums.PayMethod;
import com.example.todo.domain.entity.enums.PayStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
public class PaymentInfoDto {
    private String impUid; // 아임포트 고유 결제 번호
    private String merchantUid; // 가맹점 주문 번호
    private String name;
    private String paidAt;
    private BigDecimal paidAmount;
    private String payMethod;
    private String applyNum; // 카드 승인번호
    private String payStatus;

    public LocalDateTime convertFromUnixTimestamp(String paidAt) {
        long timestamp = Long.parseLong(paidAt);
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }
}
