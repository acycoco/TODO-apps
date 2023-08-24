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
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/subscription")
public class AdminSubscriptionApiController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponseDto> create(
            @Valid @RequestBody SubscriptionCreateRequestDto dto
    ){
        return ResponseEntity.ok(subscriptionService.createSubscription(dto));
    }

    @GetMapping
    public ResponseEntity<Page<SubscriptionResponseDto>> readAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit
    ){
        return ResponseEntity.ok(subscriptionService.readAllSubscription(page, limit));
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponseDto> read(
            @PathVariable("subscriptionId") Long subscriptionId
    ){
        return ResponseEntity.ok(subscriptionService.readSubscription(subscriptionId));
    }

    @PutMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponseDto> update(
            @PathVariable("subscriptionId") Long subscriptionId,
            @Valid @RequestBody SubscriptionCreateRequestDto dto
    ){
        return ResponseEntity.ok(subscriptionService.updateSubscription(subscriptionId, dto));
    }

    @PutMapping("/{subscriptionId}/status")
    public ResponseEntity<SubscriptionResponseDto> updateStatus(
            @PathVariable("subscriptionId") Long subscriptionId
    ){
        return ResponseEntity.ok(subscriptionService.updateSubscriptionStatus(subscriptionId));
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<ResponseDto> delete(
            @PathVariable("subscriptionId") Long subscriptionId
    ){
        subscriptionService.deleteSubscription(subscriptionId);
        return ResponseEntity.ok(new ResponseDto("구독권이 삭제되었습니다."));
    }
}
