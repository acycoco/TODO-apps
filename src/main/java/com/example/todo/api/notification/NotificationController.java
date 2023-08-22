package com.example.todo.api.notification;

import com.example.todo.dto.NotificationDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class NotificationController {
    // SSE를 저장할 리스트를 생성
    private List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    //다른 도메인으로부터 Http요청 허용
    @CrossOrigin
    //메시지를 읽을 수 있는 출력창
    @RequestMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe() {
        SseEmitter sseEmitter = new SseEmitter(0L);
        emitters.add(sseEmitter);

        // SseEmitter가 완료될 때 emitters 리스트에서 제거
        //SseEmitter는 한번 응답을 보내고 나면 완료된 상태가 되기에
        //다시 사용하기 전에 응답을 보낸 SseEmitter 객체를 제거
        sseEmitter.onCompletion(() -> {
            emitters.remove(sseEmitter);
        });
        return sseEmitter;
    }
    //아래는 Service에서 호출한다.
    @PostMapping(value = "/updateNews", produces = MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8")
    public void updateNews(@RequestBody NotificationDto notificationDto) {
        for (SseEmitter emitter : emitters) {
            try {
                SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event().data(notificationDto);
                emitter.send(eventBuilder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}