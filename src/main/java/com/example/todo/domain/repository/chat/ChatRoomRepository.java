package com.example.todo.domain.repository.chat;

import com.example.todo.domain.entity.TaskApiEntity;
import com.example.todo.domain.entity.chat.ChatRoom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByTaskApiEntity(TaskApiEntity taskApiEntity);
}
