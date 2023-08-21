package com.example.todo.api.chat;

import com.example.todo.domain.entity.chat.ChatRoom;
import com.example.todo.dto.ChatRoomDto;
import com.example.todo.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("chat")
@RequiredArgsConstructor
public class ChatRestController {
    private final ChatService chatService;

    @GetMapping("rooms/{teamId}")
    public ResponseEntity<List<ChatRoomDto>> getChatRooms(Authentication authentication,
                                                          @PathVariable("teamId") Long teamId) {

        return ResponseEntity.ok(chatService.getChatRooms(teamId));
    }

}
