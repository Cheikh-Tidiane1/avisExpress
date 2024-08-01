package com.tid.avisExpress.services;
import com.tid.avisExpress.model.Utilisateur;
import com.tid.avisExpress.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
@Slf4j
public class UtilisateurService {

    private UtilisateurRepository utilisateurRepository;

    public void inscription(Utilisateur utilisateur) {
        utilisateurRepository.save(utilisateur);
    }
}
