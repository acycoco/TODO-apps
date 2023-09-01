//package com.example.todo.service.mail;
//
//import com.example.todo.dto.mail.MailDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//public class MailService {
//
//    private final JavaMailSender javaMailSender;
//
//    @Scheduled(cron = "10 * * * * *")
//    public void mailSend() {
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setTo("dbsquddlfz@naver.com");
//        simpleMailMessage.setSubject("안녕하세요 제목입니다.");
//        simpleMailMessage.setText("내용입니다.");
//        javaMailSender.send(simpleMailMessage);
//    }
//}
