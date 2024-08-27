package com.tid.avisExpress.services;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {
    JavaMailSender mailSender;

    public void send(SimpleMailMessage message){
        this.mailSender.send(message);
    }

}
