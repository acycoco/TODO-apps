package com.example.todo.api.subscription;

import com.example.todo.domain.Response;
import com.example.todo.dto.subscription.SubscriptionCreateRequestDto;
import com.example.todo.dto.subscription.SubscriptionResponseDto;
import com.example.todo.service.subscription.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/subscription")
public class AdminSubscriptionApiController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Response<SubscriptionResponseDto> create(
            @Valid @RequestBody SubscriptionCreateRequestDto dto
    ){

        return Response.success(subscriptionService.createSubscription(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Response<Page<SubscriptionResponseDto>> readAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit
    ){
        return Response.success(subscriptionService.readAllSubscription(page - 1, limit));
    }

    @GetMapping("/{subscriptionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<SubscriptionResponseDto> read(
            @PathVariable("subscriptionId") Long subscriptionId
    ){
        return Response.success(subscriptionService.readSubscription(subscriptionId));
    }

    @PutMapping("/{subscriptionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<SubscriptionResponseDto> update(
            @PathVariable("subscriptionId") Long subscriptionId,
            @Valid @RequestBody SubscriptionCreateRequestDto dto
    ){
        return Response.success(subscriptionService.updateSubscription(subscriptionId, dto));
    }

    @PutMapping("/{subscriptionId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<SubscriptionResponseDto> updateStatus(
            @PathVariable("subscriptionId") Long subscriptionId
    ){
        return Response.success(subscriptionService.updateSubscriptionStatus(subscriptionId));
    }


}
