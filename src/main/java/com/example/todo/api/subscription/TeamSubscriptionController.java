package com.example.todo.api.subscription;

import com.example.todo.domain.Response;
import com.example.todo.dto.TeamSubscriptionResponseDto;
import com.example.todo.service.subscription.TeamSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team/{teamId}/subcription")
public class TeamSubscriptionController {
    private final TeamSubscriptionService teamSubscriptionService;

    @PostMapping("/{subscriptionId}")
    public Response<TeamSubscriptionResponseDto> create(
            @PathVariable("subscriptionId") Long subscriptionId
    )
}
