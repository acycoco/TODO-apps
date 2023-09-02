package com.example.todo.api.subscription;

import com.example.todo.dto.ResponseDto;
import com.example.todo.dto.SubscriptionCreateRequestDto;
import com.example.todo.dto.SubscriptionResponseDto;
import com.example.todo.service.subscription.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/subscription")
public class AdminSubscriptionApiController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubscriptionResponseDto> create(
            @Valid @RequestBody SubscriptionCreateRequestDto dto
    ){

        return ResponseEntity.ok(subscriptionService.createSubscription(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<SubscriptionResponseDto>> readAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit
    ){
        return ResponseEntity.ok(subscriptionService.readAllSubscription(page, limit));
    }

    @GetMapping("/{subscriptionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubscriptionResponseDto> read(
            @PathVariable("subscriptionId") Long subscriptionId
    ){
        return ResponseEntity.ok(subscriptionService.readSubscription(subscriptionId));
    }

    @PutMapping("/{subscriptionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubscriptionResponseDto> update(
            @PathVariable("subscriptionId") Long subscriptionId,
            @Valid @RequestBody SubscriptionCreateRequestDto dto
    ){
        return ResponseEntity.ok(subscriptionService.updateSubscription(subscriptionId, dto));
    }

    @PutMapping("/{subscriptionId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubscriptionResponseDto> updateStatus(
            @PathVariable("subscriptionId") Long subscriptionId
    ){
        return ResponseEntity.ok(subscriptionService.updateSubscriptionStatus(subscriptionId));
    }


}
