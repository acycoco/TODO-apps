package com.example.todo.api.Payment;

import com.example.todo.dto.TeamSubscriptionResponseDto;
import com.example.todo.service.subscription.TeamSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PaymentController {
    private final TeamSubscriptionService teamSubscriptionService;
    @GetMapping("/api/team/{teamId}/subscription/{teamSubscriptionId}/payment")
    public String showPaymentPage(
            @PathVariable Long teamId,
            @PathVariable Long teamSubscriptionId,
            Model model,
            Authentication authentication
    ) {
        TeamSubscriptionResponseDto teamSubscription = teamSubscriptionService.readTeamSubscription(teamId, teamSubscriptionId, authentication);
        model.addAttribute("teamId", teamId);
        model.addAttribute("teamSubscriptionId", teamSubscriptionId);
        model.addAttribute("teamSubscription", teamSubscription);
        return "payment";
    }

    @GetMapping("/api/choice")
    public String showChoice(

    ){
        return "subscriptionchoice";
    }
}
