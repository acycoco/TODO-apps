package com.example.todo.dto;

import com.example.todo.domain.entity.chat.ChatRoom;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatRoomDto {
    private Long id;
    private String roomName;

    public static ChatRoomDto fromEntity(ChatRoom entity) {
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.setId(entity.getId());
        chatRoomDto.setRoomName(entity.getTaskApiEntity().getTaskName());
        return chatRoomDto;
    }
}
