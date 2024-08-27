package com.tid.avisExpress.util;
import com.tid.avisExpress.model.Utilisateur;
import com.tid.avisExpress.services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import java.util.Random;

@AllArgsConstructor
@Service
public class CodeUtil {

    private NotificationService notificationService;

    public String generateCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void sendNotification(Utilisateur utilisateur, String code, String subject, String messageTemplate) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@tidiTech.com");
        message.setTo(utilisateur.getEmail());
        message.setSubject(subject);
        message.setText(String.format(messageTemplate, utilisateur.getNom(), code));
        this.notificationService.send(message);
    }

}
