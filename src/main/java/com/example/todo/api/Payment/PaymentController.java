package com.example.todo.api.Payment;

import com.example.todo.dto.UsersSubscriptionResponseDto;
import com.example.todo.service.subscription.UsersSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PaymentController {
    private final UsersSubscriptionService usersSubscriptionService;
    @GetMapping("/api/users/{userId}/subscription/{usersSubscriptionId}/payment")
    public String showPaymentPage(
            @PathVariable("userId") Long userId,
            @PathVariable("usersSubscriptionId") Long usersSubscriptionId,
            Model model
    ) {
        UsersSubscriptionResponseDto usersSubscription = usersSubscriptionService.readUsersSubscription(userId, usersSubscriptionId);
        model.addAttribute("userId", userId);
        model.addAttribute("usersSubscriptionId", usersSubscriptionId);
        model.addAttribute("usersSubscription", usersSubscription);
        return "payment";
    }

    @GetMapping("/api/choice")
    public String showChoice(

    ){
        return "subscriptionchoice";
    }
}
