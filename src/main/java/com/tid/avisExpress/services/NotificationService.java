package com.tid.avisExpress.services;

import com.tid.avisExpress.model.Validation;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    JavaMailSender mailSender;

    public void envoyer (Validation validation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@tidiTech.com");
        message.setTo(validation.getUtilisateur().getEmail());
        message.setSubject("Votre Code d'activation");
        message.setText(String.format("Bonjour %s, <br /> votre code d'activation est : %s",
                validation.getUtilisateur().getNom(),
                validation.getCode()));
        mailSender.send(message);
    }
}
