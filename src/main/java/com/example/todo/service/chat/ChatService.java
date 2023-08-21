package com.example.todo.service.chat;

import org.springframework.stereotype.Service;

@Service
public class ChatService {
    public String enterRoom(Long userId, Long teamId, Long taskId) {
        return "chat-room";
    }
}
