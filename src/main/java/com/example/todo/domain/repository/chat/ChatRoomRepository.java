package com.example.todo.domain.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRepository, Long> {
}
