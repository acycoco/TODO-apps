package com.example.todo.api.mail;

import com.example.todo.dto.mail.MailDto;
import com.example.todo.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailApiController {

    private final MailService mailService;

    @PostMapping("/mail")
    public void mail() throws InterruptedException {
        mailService.mailSend();
    }
//    @PostMapping("/mail")
//    public void mail(/*@RequestBody MailDto mailDto*/) {
//        mailService.mailSend();
//    }
}
