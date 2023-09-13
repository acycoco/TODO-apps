package com.example.todo.api.Payment;

import com.example.todo.dto.subscription.UsersSubscriptionResponseDto;
import com.example.todo.service.subscription.UsersSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/views/users-subscription/{usersSubscriptionId}")
public class PaymentViewsController {
    private final UsersSubscriptionService usersSubscriptionService;
    @GetMapping("/request-payment")
    public String requestPayment(
            @PathVariable("usersSubscriptionId") Long usersSubscriptionId,
            Authentication authentication,
            Model model
    ) {
        Long userId = Long.parseLong(authentication.getName());
        UsersSubscriptionResponseDto usersSubscription = usersSubscriptionService.readUsersSubscription(userId, usersSubscriptionId);
        model.addAttribute("usersSubscription", usersSubscription);
        return "request-payment";
    }

    @GetMapping("/payment")
    public String showPaymentDetails(
            @PathVariable("usersSubscriptionId") Long usersSubscriptionId,
            Authentication authentication,
            Model model
    ){
        model.addAttribute("usersSubscriptionId", usersSubscriptionId);
        return "payment-details";
    }



}
