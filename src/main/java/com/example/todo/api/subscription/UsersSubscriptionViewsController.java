package com.example.todo.api.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/views")
public class UsersSubscriptionViewsController {

    //구독권 등록 페이지
    @GetMapping("/register-subscription")
    public String registerSubscription(){
        return "register-subscription";
    }

    //활성화 중인 구독권 페이지
    @GetMapping("/users-subscription/active")
    public String showActiveUsersSubscription(){
        return "active-subscription";
    }

    //구독 내역 보는 페이지
    @GetMapping("/users-subscription")
    public String showUsersSubscription(){
        return "all-users-subscription";
    }

    //구독 내역 상세보기 페이지
    @GetMapping("/users-subscription/{usersSubscriptionId}")
    public String showUsersSubscriptionDetails(
            @PathVariable("usersSubscriptionId") Long usersSubscriptionId)
    {
        return "users-subscription-details";
    }

}
