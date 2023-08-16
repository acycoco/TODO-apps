package com.example.todo.domain.entity.chat;

import com.example.todo.domain.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Chat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    private String writer;
    private String content;

    @Builder
    public Chat(final ChatRoom chatRoom, final String writer, final String content) {
        this.chatRoom = chatRoom;
        this.writer = writer;
        this.content = content;
    }
}
