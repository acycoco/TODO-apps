package com.example.todo.api.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/update-subscription")
    public String updateSubscription(){
        return "update-subscription";
    }

    @GetMapping("/subscription")
    public String shoewSubscription(){
        return "subscription";
    }
}
