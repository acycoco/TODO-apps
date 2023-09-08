package com.example.todo.service.notification;

import com.example.todo.domain.entity.TaskApiEntity;
import com.example.todo.domain.entity.TaskCommentEntity;
import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.repository.NotificationRepository;
import com.example.todo.domain.repository.TaskCommentRepository;
import com.example.todo.domain.repository.TeamReposiotry;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationService {
    // 기본 타임아웃 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final NotificationRepository notificationRepository;
    private final TaskCommentRepository taskCommentRepository;
    private final TeamReposiotry teamRepository;
    // 구독하는 클라이언트 아이디에 대한 SseEmitter를 생성하여 반환
    public SseEmitter subscribe(Long userId, final HttpServletResponse response) {
        SseEmitter emitter = createEmitter(userId);
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        //무응답 오류방지
        sendToClient(userId, "EventStream 변경. [userId=" + userId + "]");
        return emitter;
    }

    //서버의 이벤트를 클라이언트에게 보낸다

    public void notify(Long sendId, Object event) {
        sendToClient(sendId, event);
    }

    //데이터를 클라이언트에게 보낸다.
    private void sendToClient(Long sendId, Object data) {
        // 먼저 클라이언트의 SseEmitter를 가져온다
        SseEmitter emitter = notificationRepository.get(sendId);
        if (emitter != null) {
            try {
                // 데이터를 클라이언트에게 실어보낸다.
                emitter.send(SseEmitter.event().id(String.valueOf(sendId)).name("알림").data(data));
            } catch (IOException exception) {
                // 데이터 전송 중 오류가 발생하면 Emitter를 삭제하고 에러를 완료 상태로 처리
                notificationRepository.deleteById(sendId);
                emitter.completeWithError(exception);
            }
        }
    }

    //클라이언트를 위한 SseEmitter를 생성
    private SseEmitter createEmitter(Long id) {
        //타임아웃 설정
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        //생성된 SseEmitter를 저장소에 저장
        notificationRepository.save(id, emitter);

        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
        emitter.onCompletion(() -> notificationRepository.deleteById(id));
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() -> notificationRepository.deleteById(id));

        return emitter;
    }
}