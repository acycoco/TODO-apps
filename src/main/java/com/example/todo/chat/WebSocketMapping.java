package com.example.todo.chat;

import com.example.todo.domain.entity.MemberEntity;
import com.example.todo.domain.entity.TaskApiEntity;
import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.chat.Chat;
import com.example.todo.domain.entity.chat.ChatRoom;

import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.MemberRepository;
import com.example.todo.domain.repository.TaskApiRepository;
import com.example.todo.domain.repository.TeamReposiotry;
import com.example.todo.domain.repository.chat.ChatRepository;
import com.example.todo.domain.repository.chat.ChatRoomRepository;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.ChatMessageDto;
import com.example.todo.service.chat.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class WebSocketMapping {
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final TeamReposiotry teamReposiotry;
    private final TaskApiRepository taskApiRepository;
    private final ChatService chatService;

    public WebSocketMapping(UserRepository userRepository, MemberRepository memberRepository, SimpMessagingTemplate simpMessagingTemplate, ChatRepository chatRepository, ChatRoomRepository chatRoomRepository, TeamReposiotry teamReposiotry, TaskApiRepository taskApiRepository, ChatService chatService) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatRepository = chatRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.teamReposiotry = teamReposiotry;
        this.taskApiRepository = taskApiRepository;
        this.chatService = chatService;
    }

    @MessageMapping("/chat")
    public void sendChat(Authentication authentication,
                         ChatMessageDto chatMessage,
                         @PathVariable("teamId") Long teamId,
                         @PathVariable("taskId") Long taskId) {

        Long userId = Long.parseLong(authentication.getName());
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        User user = optionalUser.get();

        Optional<TeamEntity> teamEntityOptional = teamReposiotry.findById(teamId);
        if (teamEntityOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 팀은 존재하지 않습니다!");
        TeamEntity teamEntity = teamEntityOptional.get();

        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByTeamAndUser(teamEntity, user);
        if (optionalMemberEntity.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저는 해당 팀에 속한 멤버가 아닙니다!");
        MemberEntity memberEntity = optionalMemberEntity.get();

        Optional<TaskApiEntity> optionalTaskApiEntity = taskApiRepository.findById(taskId);
        if (optionalTaskApiEntity.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 task는 존재하지 않습니다!");
        TaskApiEntity taskApiEntity = optionalTaskApiEntity.get();

        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findByTaskApiEntity(taskApiEntity);
        if (optionalChatRoom.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 채팅방은 존재하지 않습니다!");
        ChatRoom chatRoom = optionalChatRoom.get();

        Chat chat = new Chat(chatRoom, user.getUsername(), chatMessage.getMessage());
        chatRepository.save(chat);

        log.info(chatMessage.toString());
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        chatMessage.setTime(time);

        simpMessagingTemplate.convertAndSend(
                String.format("/%d/task/%d", teamId, taskId),
                chatMessage
        );
    }
    // 누군가가 구독할때 실행하는 메소드
    @SubscribeMapping("/{roomId}")
    public List<ChatMessageDto> sendGreet(Authentication authentication,
                                          @DestinationVariable("roomId") Long roomId
    ) {
        log.info("new subscription to {}", roomId);
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(roomId);
        if (optionalChatRoom.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 채팅방은 존재하지 않습니다!");
        ChatRoom chatRoom = optionalChatRoom.get();

        Optional<User> optionalUser = userRepository.findById(Long.parseLong(authentication.getName()));
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저는 존재하지 않습니다!");
        User user = optionalUser.get();
        String username = user.getUsername();
        List<ChatMessageDto> lastMessages
                = chatService.getLastMessages(roomId);
        ChatMessageDto chatMessage = new ChatMessageDto();
        chatMessage.setRoom(chatRoom);
        chatMessage.setSender("admin");
        chatMessage.setMessage(String.format("%s님이 입장하셨습니다.", username));
        lastMessages.add(chatMessage);

        return lastMessages;
    }
}
