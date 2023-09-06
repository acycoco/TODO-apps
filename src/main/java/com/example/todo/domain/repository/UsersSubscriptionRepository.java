package com.example.todo.domain.repository;

import com.example.todo.domain.entity.UsersSubscriptionEntity;
import com.example.todo.domain.entity.enums.SubscriptionStatus;
import com.example.todo.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UsersSubscriptionRepository extends JpaRepository<UsersSubscriptionEntity, Long> {
    List<UsersSubscriptionEntity> findAllByUsers(User user);
    Optional<UsersSubscriptionEntity> findByUsersAndSubscriptionStatus(User user, SubscriptionStatus status);
    Boolean existsByUsersAndSubscriptionStatus(User user, SubscriptionStatus status);
    Optional<UsersSubscriptionEntity> findByMerchantUid(String merchantUid);
    List<UsersSubscriptionEntity> findAllByEndDateBeforeAndSubscriptionStatus(LocalDate localDate, SubscriptionStatus status);

    @Query("select us " +
            "from UsersSubscriptionEntity us " +
            "where us.subscriptionStatus = 'ACTIVE' " +
            "and us.id between 2 and 10000" +
            "and us.endDate =:week")
    List<UsersSubscriptionEntity> customFindAll(@Param("week") LocalDate week);
//    @Query("select us " +
//            "from UsersSubscriptionEntity us " +
//            "where us.subscriptionStatus = 'ACTIVE' " +
//            "and us.id = 2 or us.id = 3")
//    List<UsersSubscriptionEntity> customFindAll();
}
