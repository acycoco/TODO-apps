package com.example.todo.service.subscription;

import com.example.todo.domain.entity.SubscriptionEntity;
import com.example.todo.domain.entity.UsersSubscriptionEntity;
import com.example.todo.domain.entity.enums.SubscriptionStatus;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.*;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.UsersSubscriptionResponseDto;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersSubscriptionService {
    private final UsersSubscriptionRepository usersSubscriptionRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    private String generateMerchantUid(){
        Instant instant = Instant.now();
        Long timestamp = instant.toEpochMilli();
        return "order_" + timestamp;
    }
//    private String generateMerchantUid(){
//        return "order_" + UUID.randomUUID();
//    }

    @Transactional
    public UsersSubscriptionResponseDto createUsersSubscription(Long userId, Long subscriptionId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));

        SubscriptionEntity subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_SUBSCRIPTION));

        //users_subscription PENDING 상태로 생성
        UsersSubscriptionEntity usersSubscription = UsersSubscriptionEntity.builder()
                .users(user)
                .subscription(subscription)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .subscriptionStatus(SubscriptionStatus.PENDING)
                .merchantUid(generateMerchantUid())
                .subscriptionPrice(subscription.getPrice())
                .build();

        return UsersSubscriptionResponseDto.fromEntity(usersSubscriptionRepository.save(usersSubscription));
    }

    @Transactional
    public Page<UsersSubscriptionResponseDto> readAllUsersSubscription(Long userId, Integer page, Integer limit){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));

        //findAllByUsers가 맞는지 findAllByUsersIs가 맞는지..
        List<UsersSubscriptionEntity> usersSubscriptions = usersSubscriptionRepository.findAllByUsers(user);
        Page<UsersSubscriptionEntity> usersSubscriptionPages = new PageImpl<>(usersSubscriptions, PageRequest.of(page, limit), usersSubscriptions.size());
        return usersSubscriptionPages.map(UsersSubscriptionResponseDto::fromEntity);
    }

    @Transactional
    public UsersSubscriptionResponseDto readUsersSubscription(Long userId, Long usersSubscriptionId){
        if (!userRepository.existsById(userId))
            throw new TodoAppException(ErrorCode.NOT_FOUND_USER);

        UsersSubscriptionEntity usersSubscription = usersSubscriptionRepository.findById(usersSubscriptionId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USERS_SUBSCRIPTION));

        if (!userId.equals(usersSubscription.getUsers().getId()))
            throw new TodoAppException(ErrorCode.NOT_MATCH_USERS_AND_USERS_SUBSCRIPTION);

        return UsersSubscriptionResponseDto.fromEntity(usersSubscription);
    }

    @Transactional
    public UsersSubscriptionResponseDto readUsersSubscriptionActive(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));

        UsersSubscriptionEntity usersActiveSubscription = usersSubscriptionRepository.findByUsersAndSubscriptionStatus(user, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_ACTIVE_SUBSCRIPTION));

        return UsersSubscriptionResponseDto.fromEntity(usersActiveSubscription);
    }

    @Transactional
    public UsersSubscriptionResponseDto updateUsersSubscriptionExpired(Long userId, Long usersSubscriptionId){
        if (!userRepository.existsById(userId))
            throw new TodoAppException(ErrorCode.NOT_FOUND_USER);

        UsersSubscriptionEntity usersSubscription = usersSubscriptionRepository.findById(usersSubscriptionId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USERS_SUBSCRIPTION));

        if (!userId.equals(usersSubscription.getUsers().getId()))
            throw new TodoAppException(ErrorCode.NOT_MATCH_USERS_AND_USERS_SUBSCRIPTION);

        //만료로 상태변경
        usersSubscription.changeSubscriptionStatus(SubscriptionStatus.EXPIRED);

        return UsersSubscriptionResponseDto.fromEntity(usersSubscriptionRepository.save(usersSubscription));
    }


    //만료 상태로 자동으로 바꿔주는 스케줄러
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void scheduleUsersSubscriptionExpired(){

        List<UsersSubscriptionEntity> expiredUsersSubscriptions =
                usersSubscriptionRepository.findAllByEndDateBeforeAndSubscriptionStatus(LocalDate.now(), SubscriptionStatus.ACTIVE);
        for (UsersSubscriptionEntity expiredUsersSubscription : expiredUsersSubscriptions) {

            //만료로 상태변경
            expiredUsersSubscription.changeSubscriptionStatus(SubscriptionStatus.EXPIRED);
            usersSubscriptionRepository.save(expiredUsersSubscription);
        }
    }

    @Transactional
    public UsersSubscriptionResponseDto updateUsersSubscriptionCanceled(Long userId, Long usersSubscriptionId){
        if (!userRepository.existsById(userId))
            throw new TodoAppException(ErrorCode.NOT_FOUND_USER);

        UsersSubscriptionEntity usersSubscription = usersSubscriptionRepository.findById(usersSubscriptionId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USERS_SUBSCRIPTION));

        if (!userId.equals(usersSubscription.getUsers().getId()))
            throw new TodoAppException(ErrorCode.NOT_MATCH_USERS_AND_USERS_SUBSCRIPTION);

        //취소로 상태변경
        usersSubscription.changeSubscriptionStatus(SubscriptionStatus.CANCELED);

        return UsersSubscriptionResponseDto.fromEntity(usersSubscriptionRepository.save(usersSubscription));
    }

}
