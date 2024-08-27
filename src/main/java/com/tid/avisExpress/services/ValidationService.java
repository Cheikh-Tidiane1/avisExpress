package com.tid.avisExpress.services;
import com.tid.avisExpress.model.Utilisateur;
import com.tid.avisExpress.model.Validation;
import com.tid.avisExpress.repository.ValidationRepository;
import com.tid.avisExpress.util.CodeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import static java.time.temporal.ChronoUnit.MINUTES;

@Transactional
@AllArgsConstructor
@Service
@Slf4j
public class ValidationService {

    private ValidationRepository validationRepository;
    private CodeUtil codeUtil ;

    public void enregistrer(Utilisateur utilisateur) {
        String code = codeUtil.generateCode();
        Validation validation = Validation.builder()
                .code(code)
                .utilisateur(utilisateur)
                .creation(Instant.now())
                .expire(Instant.now().plus(10, MINUTES))
                .build();
        this.validationRepository.save(validation);
        this.codeUtil.sendNotification(utilisateur, code, "Votre Code d'activation", "Bonjour %s, <br /> votre code d'activation est : %s");
    }


    public Validation valideCode  (String code){
       return  this.validationRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Le code " + code + " n'existe pas"));
    }
}
