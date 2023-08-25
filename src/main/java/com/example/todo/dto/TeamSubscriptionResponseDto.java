package com.example.todo.dto;

import com.example.todo.domain.entity.TeamActiveSubscriptionEntity;
import com.example.todo.domain.entity.TeamSubscriptionEntity;
import com.example.todo.domain.entity.enums.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;

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

    public static TeamSubscriptionResponseDto fromEntity(TeamSubscriptionEntity entity){
        TeamSubscriptionResponseDto dto = new TeamSubscriptionResponseDto();
        dto.setId(entity.getId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setSubscriptionStatus(entity.getSubscriptionStatus());
        dto.setTeamName(entity.getTeam().getName());
        dto.setSubscriptionName(entity.getSubscription().getName());
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
        return dto;
    }

}
