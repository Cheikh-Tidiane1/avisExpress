package com.tid.avisExpress.services;
import com.tid.avisExpress.model.PasswordReset;
import com.tid.avisExpress.model.Utilisateur;
import com.tid.avisExpress.repository.PasswordResetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Random;
import static java.time.temporal.ChronoUnit.MINUTES;

@AllArgsConstructor
@Service
public class PasswordResetService {

    private PasswordResetRepository passwordResetRepository;
    private NotificationService notificationService;

    public void saveNewPasswordReset(Utilisateur utilisateur) {
        PasswordReset passwordReset = PasswordReset.builder()
                .utilisateur(utilisateur)
                .expire(Instant.now().plus(10,MINUTES))
                .creation(Instant.now())
                .code(String.format("%06d", new Random().nextInt(999999)))
                .build();
        this.passwordResetRepository.save(passwordReset);
        this.notificationService.envoyerMdpCode(passwordReset);
    }

    public PasswordReset validCode  (String code){
        return  this.passwordResetRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Le code " + code + " n'existe pas"));
    }
}
