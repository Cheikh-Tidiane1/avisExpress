package com.tid.avisExpress.services;
import com.tid.avisExpress.model.PasswordReset;
import com.tid.avisExpress.model.Utilisateur;
import com.tid.avisExpress.repository.PasswordResetRepository;
import com.tid.avisExpress.util.CodeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import static java.time.temporal.ChronoUnit.MINUTES;

@Transactional
@AllArgsConstructor
@Service
@Slf4j
public class PasswordResetService {

    private PasswordResetRepository passwordResetRepository;
    private CodeUtil codeUtil ;

    public void saveNewPasswordReset(Utilisateur utilisateur) {
        String code = codeUtil.generateCode();
        PasswordReset passwordReset = PasswordReset.builder()
                .utilisateur(utilisateur)
                .expire(Instant.now().plus(10, MINUTES))
                .creation(Instant.now())
                .code(code)
                .build();
        this.passwordResetRepository.save(passwordReset);
        codeUtil.sendNotification(utilisateur, code, "Password reset", "Bonjour %s, <br /> votre code mdp : %s");
    }

    public PasswordReset validCode  (String code){
        return  this.passwordResetRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Le code " + code + " n'existe pas"));
    }

    @Scheduled(cron = "0 1 * * * *")
    public void cleanTableCode(){
        Instant now = Instant.now();
        log.info("Cleaning table code at {} " ,   now);
        this.passwordResetRepository.deleteAllByExpireBefore(now);
    }

}
