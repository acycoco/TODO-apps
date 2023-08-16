package com.example.todo.domain.repository.chat;

import com.example.todo.domain.entity.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
