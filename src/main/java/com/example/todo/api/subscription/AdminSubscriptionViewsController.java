package com.example.todo.api.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/views/admin")
public class AdminSubscriptionViewsController {
    //admin
    @GetMapping("/create-subscription")
    public String createSubscription(){
        return "create-subscription";
    }

    @GetMapping("/update-subscription/{subscriptionId}")
    public String updateSubscription(
            @PathVariable("subscriptionId") Long subscriptionId,
            Model model
    ){
        model.addAttribute("subscriptionId", subscriptionId);
        return "update-subscription";
    }

    @GetMapping("/subscription")
    public String shoewSubscription(){
        return "all-subscription";
    }

    @GetMapping("/subscription/{subscriptionId}")
    public String shoewSubscriptionDetails(
            @PathVariable("subscriptionId") Long subscriptionId,
            Model model
    ){
        model.addAttribute("subscriptionId", subscriptionId);
        return "subscription-details";
    }
}
