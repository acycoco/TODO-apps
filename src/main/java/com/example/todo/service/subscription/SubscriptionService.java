package com.example.todo.service.subscription;

import com.example.todo.domain.entity.SubscriptionEntity;
import com.example.todo.domain.repository.SubscriptionRepository;
import com.example.todo.dto.SubscriptionCreateRequestDto;
import com.example.todo.dto.SubscriptionResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public SubscriptionResponseDto createSubscription(SubscriptionCreateRequestDto dto){
        SubscriptionEntity subscription = SubscriptionEntity.builder()
                .name(dto.getName())
                .maxMember(dto.getMaxMember())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .status(true)
                .build();

        return SubscriptionResponseDto.fromEntity(subscriptionRepository.save(subscription));
    }
    @Transactional
    public Page<SubscriptionResponseDto> readAllSubscription(Integer page, Integer limit){
        Page<SubscriptionEntity> subscriptionEntities = subscriptionRepository.findAll(PageRequest.of(page, limit));
        return subscriptionEntities.map(SubscriptionResponseDto::fromEntity);
    }
    @Transactional
    public SubscriptionResponseDto readSubscription(Long subscriptionId){
        SubscriptionEntity subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 구독권이 존재하지 않습니다. "));

        return SubscriptionResponseDto.fromEntity(subscription);
    }

    @Transactional
    public Page<SubscriptionResponseDto> readAllActiveSubscription(Integer page, Integer limit){
        Page<SubscriptionEntity> subscriptionEntities = subscriptionRepository.findAllByStatusIsTrue(PageRequest.of(page, limit));
        return subscriptionEntities.map(SubscriptionResponseDto::fromEntity);
    }
    @Transactional
    public SubscriptionResponseDto readActiveSubscription(Long subscriptionId){
        SubscriptionEntity subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 구독권이 존재하지 않습니다. "));

        if (!subscription.getStatus())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"구독권이 비활성화 상태입니다. ");

        return SubscriptionResponseDto.fromEntity(subscription);
    }
    @Transactional
    public SubscriptionResponseDto updateSubscription(Long subscriptionId, SubscriptionCreateRequestDto dto){
        SubscriptionEntity subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 구독권이 존재하지 않습니다. "));

        subscription.changeName(dto.getName());
        subscription.changeMaxMember(dto.getMaxMember());
        subscription.changePrice(dto.getPrice());
        subscription.changeDescription(dto.getDescription());

        return SubscriptionResponseDto.fromEntity(subscriptionRepository.save(subscription));
    }
    @Transactional
    public SubscriptionResponseDto updateSubscriptionStatus(Long subscriptionId){
        SubscriptionEntity subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 구독권이 존재하지 않습니다. "));

        if (subscription.getStatus()){
            subscription.changeStatus(false);
        } else {
            subscription.changeStatus(true);
        }

        return SubscriptionResponseDto.fromEntity(subscriptionRepository.save(subscription));
    }
    @Transactional
    public void deleteSubscription(Long subscriptionId){
        SubscriptionEntity subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 구독권이 존재하지 않습니다. "));

        subscriptionRepository.deleteById(subscriptionId);
    }
}
