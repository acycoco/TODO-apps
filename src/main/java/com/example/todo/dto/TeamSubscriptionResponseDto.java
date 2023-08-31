package com.example.todo.dto;

import com.example.todo.domain.entity.TeamActiveSubscriptionEntity;
import com.example.todo.domain.entity.TeamSubscriptionEntity;
import com.example.todo.domain.entity.enums.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TeamSubscriptionResponseDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private SubscriptionStatus subscriptionStatus;
    private String teamName;
    private String subscriptionName;
    private String merchantUid;
    private BigDecimal SubscriptionPrice;
    private String username;

    public static TeamSubscriptionResponseDto fromEntity(TeamSubscriptionEntity entity){
        TeamSubscriptionResponseDto dto = new TeamSubscriptionResponseDto();
        dto.setId(entity.getId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setSubscriptionStatus(entity.getSubscriptionStatus());
        dto.setTeamName(entity.getTeam().getName());
        dto.setSubscriptionName(entity.getSubscription().getName());
        dto.setMerchantUid(entity.getMerchantUid());
        dto.setSubscriptionPrice(entity.getSubscriptionPrice());
        dto.setUsername(entity.getTeam().getManager().getUsername());
        return dto;
    }

    public static TeamSubscriptionResponseDto fromActiveEntity(TeamActiveSubscriptionEntity entity){
        TeamSubscriptionResponseDto dto = new TeamSubscriptionResponseDto();
        dto.setId(entity.getId());
        dto.setStartDate(entity.getTeamSubscription().getStartDate());
        dto.setEndDate(entity.getTeamSubscription().getEndDate());
        dto.setSubscriptionStatus(entity.getTeamSubscription().getSubscriptionStatus());
        dto.setTeamName(entity.getTeam().getName());
        dto.setSubscriptionName(entity.getTeamSubscription().getSubscription().getName());
        dto.setMerchantUid(entity.getTeamSubscription().getMerchantUid());
        dto.setSubscriptionPrice(entity.getTeamSubscription().getSubscriptionPrice());
        dto.setUsername(entity.getTeam().getManager().getUsername());
        return dto;
    }

}
