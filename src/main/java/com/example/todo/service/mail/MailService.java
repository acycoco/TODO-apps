package com.example.todo.service.mail;

import com.example.todo.domain.entity.UsersSubscriptionEntity;
import com.example.todo.domain.repository.UsersSubscriptionRepository;
import com.example.todo.dto.mail.MailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final UsersSubscriptionRepository usersSubscriptionRepository;

//    @Scheduled(cron = "10 * * * * *")
//    @Async
    public void mailSend() {
        LocalDate localDate = LocalDate.of(2023, 9, 12);
        List<UsersSubscriptionEntity> all = usersSubscriptionRepository.customFindAll(localDate);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo("dbsquddlfz@naver.com");
            simpleMailMessage.setSubject("안녕하세요 제목입니다." + all.get(i).getId());
            simpleMailMessage.setText("내용입니다.");

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                javaMailSender.send(simpleMailMessage);
            });

            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            allOf.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
//    @Async
//    public void mailSend() {
//        LocalDate localDate = LocalDate.of(2023, 9, 12);
//        List<UsersSubscriptionEntity> all = usersSubscriptionRepository.customFindAll(localDate);
//        for (int i = 0; i < 10; i++) {
//            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//            simpleMailMessage.setTo("dbsquddlfz@naver.com");
//            simpleMailMessage.setSubject("안녕하세요 제목입니다." + all.get(i).getId());
//            simpleMailMessage.setText("내용입니다.");
//            javaMailSender.send(simpleMailMessage);
//        }
//    }
}
