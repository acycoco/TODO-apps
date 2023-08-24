package com.example.todo.domain.repository.chat;

import com.example.todo.domain.entity.chat.Chat;
import com.example.todo.domain.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByChatRoomOrderByIdDesc(ChatRoom room);
}
