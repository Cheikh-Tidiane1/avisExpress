package com.tid.avisExpress.services;
import com.tid.avisExpress.model.Utilisateur;
import com.tid.avisExpress.model.Validation;
import com.tid.avisExpress.repository.ValidationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;
@Transactional
@AllArgsConstructor
@Service
@Slf4j
public class ValidationService {

    private ValidationRepository validationRepository;
    private NotificationService notificationService;
    public void enregistrer (Utilisateur utilisateur) {
        Validation validation = Validation.builder()
                .code(String.format("%06d", new Random().nextInt(999999)))
                .utilisateur(utilisateur)
                .creation(Instant.now())
                .expire(Instant.now().plus(10,MINUTES))
                .build();
        this.validationRepository.save(validation);
        this.notificationService.envoyer(validation);
    }

    public Validation valideCode  (String code){
       return  this.validationRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Le code " + code + " n'existe pas"));
    }
}
