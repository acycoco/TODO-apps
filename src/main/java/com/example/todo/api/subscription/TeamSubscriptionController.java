package com.example.todo.api.subscription;

import com.example.todo.domain.Response;
import com.example.todo.dto.TeamSubscriptionResponseDto;
import com.example.todo.service.subscription.TeamSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team/{teamId}/subscription")
public class TeamSubscriptionController {
    private final TeamSubscriptionService teamSubscriptionService;

    @PostMapping("/{subscriptionId}")
    public Response<TeamSubscriptionResponseDto> create(
            @PathVariable("teamId") Long teamId,
            @PathVariable("subscriptionId") Long subscriptionId,
            Authentication authentication
    ){
        return Response.success(
                teamSubscriptionService.createTeamSubscription(teamId, subscriptionId, authentication)
        );
    }

    @GetMapping
    public Response<Page<TeamSubscriptionResponseDto>> readAll(
            @PathVariable("teamId") Long teamId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit,
            Authentication authentication
    ){
        return Response.success(
                teamSubscriptionService.readAllTeamSubscription(teamId, page, limit , authentication)
        );
    }

    @GetMapping("/{teamSubscriptionId}")
    public Response<TeamSubscriptionResponseDto> read(
            @PathVariable("teamId") Long teamId,
            @PathVariable("teamSubscriptionId") Long teamSubscriptionId,
            Authentication authentication
    ){
        return Response.success(
                teamSubscriptionService.readTeamSubscription(teamId, teamSubscriptionId, authentication)
        );
    }

    @GetMapping("/active-subscription")
    public Response<TeamSubscriptionResponseDto> readActive(
            @PathVariable("teamId") Long teamId,
            Authentication authentication
    ){
        return Response.success(
                teamSubscriptionService.readTeamActiveSubscription(teamId, authentication)
        );
    }

    @PutMapping("/{teamSubscriptionId}")
    public Response<TeamSubscriptionResponseDto> updateExpired(
            @PathVariable("teamId") Long teamId,
            @PathVariable("teamSubscriptionId") Long teamSubscriptionId,
            Authentication authentication
    ){
        return Response.success(
                teamSubscriptionService.updateTeamSubscriptionExpired(teamId, teamSubscriptionId, authentication)
        );
    }


}
