package com.example.todo.dto;

import com.example.todo.domain.entity.chat.Chat;
import com.example.todo.domain.entity.chat.ChatRoom;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChatMessageDto {
    private ChatRoom room;
    private String sender;
    private String message;
    private String time;

    public static ChatMessageDto fromEntity(Chat chatEntity) {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setRoom(chatMessageDto.getRoom());
        chatMessageDto.setSender(chatEntity.getWriter());
        chatMessageDto.setMessage(chatEntity.getContent());
        chatMessageDto.setTime(String.valueOf(chatEntity.getCreatedAt()));
        return chatMessageDto;
    }

    public Chat newEntity() {
        Chat chat = new Chat(room, sender, message);
        return chat;
    }
}
