package com.example.todo.dto;


import com.example.todo.domain.entity.SubscriptionEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubscriptionResponseDto {
    private Long id;
    private String name;
    private Integer maxMember;
    private Integer price;
    private String description;
    private Boolean status;

    public static SubscriptionResponseDto fromEntity(SubscriptionEntity entity){
        SubscriptionResponseDto dto = new SubscriptionResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setMaxMember(entity.getMaxMember());
        dto.setPrice(entity.getPrice());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
