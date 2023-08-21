package com.example.todo.api.chat;

import com.example.todo.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    @GetMapping("/team/{teamId}/tasks/{taskId}")
    public String enterRoom(Authentication authentication,
                            @PathVariable("teamId") Long teamId,
                            @PathVariable("taskId") Long taskId) {
        Long userId = Long.parseLong(authentication.getName());
        return chatService.enterRoom(userId, teamId, taskId);
    }
}
