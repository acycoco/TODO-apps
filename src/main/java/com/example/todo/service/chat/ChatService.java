package com.example.todo.service.chat;

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
import com.example.todo.dto.ChatRoomDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final TeamReposiotry teamReposiotry;
    private final MemberRepository memberRepository;
    private final TaskApiRepository taskApiRepository;
    public void createRoom(TaskApiEntity taskApiEntity) {
        log.info(taskApiEntity.getUserId().toString());

        Optional<User> optionalUser = userRepository.findById(taskApiEntity.getUserId());
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "create chatroom 유저 조회X");
        User user = optionalUser.get();

        log.info("2");
        Optional<TeamEntity> teamEntityOptional = teamReposiotry.findById(taskApiEntity.getTeam().getId());
        if (teamEntityOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 팀은 존재하지 않습니다!");
        TeamEntity teamEntity = teamEntityOptional.get();

        log.info("3");
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByTeamAndUser(teamEntity, user);
        if (optionalMemberEntity.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저는 해당 팀에 속한 멤버가 아닙니다!");

        log.info("4");
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTaskApiEntity(taskApiEntity);
        chatRoomRepository.save(chatRoom);
    }

    public void saveChatMessage(ChatMessageDto chatMessageDto) {
        chatRepository.save(chatMessageDto.newEntity());
    }
    public String enterRoom(Long userId, Long teamId, Long taskId) {
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

        return "chat-room";
    }

    // 채팅 기록 불러오기
    public List<ChatMessageDto> getLastMessages(Long roomId) {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(roomId);
        if (optionalChatRoom.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당 채팅방이 존재하지 않습니다!");
        ChatRoom room = optionalChatRoom.get();

        List<ChatMessageDto> chatMessages = new ArrayList<>();
        List<Chat> chatMessageEntities = chatRepository.findAllByChatRoomOrderByIdDesc(room);
        Collections.reverse(chatMessageEntities);
        for (Chat messageEntity: chatMessageEntities) {
            chatMessages.add(ChatMessageDto.fromEntity(messageEntity));
        }
        return chatMessages;
    }

    // teamId로 채팅방 조회하기
    public List<ChatRoomDto> getChatRooms(Long teamId) {
        List<ChatRoomDto> chatRoomDtoList = new ArrayList<>();
        for (ChatRoom chatRoomEntity: chatRoomRepository.findAll()) {
            chatRoomDtoList.add(ChatRoomDto.fromEntity(chatRoomEntity));
        }
        return chatRoomDtoList;
    }
}
