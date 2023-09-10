package com.example.todo.dto.subscription;


import com.example.todo.domain.entity.SubscriptionEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SubscriptionCreateRequestDto {
    @NotNull(message = "이름을 작성해주세요.")
    private String name;
    @Min(0) @NotNull
    private Integer maxMember;
    @Min(0) @NotNull
    private BigDecimal price;
    private String description;

    public static SubscriptionCreateRequestDto fromEntity(SubscriptionEntity entity){
        SubscriptionCreateRequestDto dto = new SubscriptionCreateRequestDto();
        dto.setName(entity.getName());
        dto.setMaxMember(entity.getMaxMember());
        dto.setPrice(entity.getPrice());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}
