package com.example.todo.api.subscription;

import com.example.todo.domain.Response;
import com.example.todo.dto.UsersSubscriptionResponseDto;
import com.example.todo.service.subscription.UsersSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/subscription")
public class UsersSubscriptionController {
    private final UsersSubscriptionService usersSubscriptionService;

    @PostMapping("/{subscriptionId}")
    public Response<UsersSubscriptionResponseDto> create(
            @PathVariable("userId") Long userId,
            @PathVariable("subscriptionId") Long subscriptionId
    ){
        return Response.success(
                usersSubscriptionService.createUsersSubscription(userId, subscriptionId)
        );
    }

    @GetMapping
    public Response<Page<UsersSubscriptionResponseDto>> readAll(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit
    ){
        return Response.success(
                usersSubscriptionService.readAllUsersSubscription(userId, page, limit)
        );
    }

    @GetMapping("/{usersSubscriptionId}")
    public Response<UsersSubscriptionResponseDto> read(
            @PathVariable("userId") Long userId,
            @PathVariable("usersSubscriptionId") Long usersSubscriptionId
    ){
        return Response.success(
                usersSubscriptionService.readUsersSubscription(userId, usersSubscriptionId)
        );
    }

    @GetMapping("/active-subscription")
    public Response<UsersSubscriptionResponseDto> readActive(
            @PathVariable("userId") Long userId
    ){
        return Response.success(
                usersSubscriptionService.readUsersSubscriptionActive(userId)
        );
    }

    @PutMapping("/{usersSubscriptionId}")
    public Response<UsersSubscriptionResponseDto> updateExpired(
            @PathVariable("userId") Long userId,
            @PathVariable("usersSubscriptionId") Long usersSubscriptionId
    ){
        return Response.success(
                usersSubscriptionService.updateUsersSubscriptionExpired(userId, usersSubscriptionId)
        );
    }


}
