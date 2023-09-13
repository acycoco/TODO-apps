package com.example.todo.api.subscription;

import com.example.todo.domain.Response;
import com.example.todo.dto.subscription.UsersSubscriptionResponseDto;
import com.example.todo.service.subscription.UsersSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users-subscription")
public class UsersSubscriptionApiController {
    private final UsersSubscriptionService usersSubscriptionService;

    @PostMapping("/{subscriptionId}")
    public Response<UsersSubscriptionResponseDto> create(
            @PathVariable("subscriptionId") Long subscriptionId,
            Authentication authentication
    ){
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(
                usersSubscriptionService.createUsersSubscription(userId, subscriptionId)
        );
    }

    @GetMapping
    public Response<Page<UsersSubscriptionResponseDto>> readAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            Authentication authentication
    ){
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(
                usersSubscriptionService.readAllUsersSubscription(userId, page, limit)
        );
    }

    @GetMapping("/{usersSubscriptionId}")
    public Response<UsersSubscriptionResponseDto> read(
            @PathVariable("usersSubscriptionId") Long usersSubscriptionId,
            Authentication authentication
    ){
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(
                usersSubscriptionService.readUsersSubscription(userId, usersSubscriptionId)
        );
    }

    @GetMapping("/active")
    public Response<UsersSubscriptionResponseDto> readActive(
            Authentication authentication
    ){
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(
                usersSubscriptionService.readUsersSubscriptionActive(userId)
        );
    }

    @PutMapping("/{usersSubscriptionId}")
    public Response<UsersSubscriptionResponseDto> updateExpired(
            @PathVariable("usersSubscriptionId") Long usersSubscriptionId,
            Authentication authentication
    ){
        Long userId = Long.parseLong(authentication.getName());
        return Response.success(
                usersSubscriptionService.updateUsersSubscriptionExpired(userId, usersSubscriptionId)
        );
    }


}
