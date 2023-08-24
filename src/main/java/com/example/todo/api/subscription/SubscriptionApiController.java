package com.example.todo.api.subscription;

import com.example.todo.dto.SubscriptionResponseDto;
import com.example.todo.service.subscription.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription")
public class SubscriptionApiController {
    private final SubscriptionService subscriptionService;
    @GetMapping
    public ResponseEntity<Page<SubscriptionResponseDto>> readAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit
    ){
        return ResponseEntity.ok(subscriptionService.readAllActiveSubscription(page, limit));
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponseDto> read(
            @PathVariable("subscriptionId") Long subscriptionId
    ){
        return ResponseEntity.ok(subscriptionService.readActiveSubscription(subscriptionId));
    }
}
