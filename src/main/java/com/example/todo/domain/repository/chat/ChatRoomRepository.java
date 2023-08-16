package com.example.todo.domain.repository.chat;

import com.example.todo.domain.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
