package com.tid.avisExpress.services;
import com.tid.avisExpress.model.Role;
import com.tid.avisExpress.model.TypeDeRole;
import com.tid.avisExpress.model.Utilisateur;
import com.tid.avisExpress.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class UtilisateurService {

    private UtilisateurRepository utilisateurRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private ValidationService validationService;

    public void inscription(Utilisateur utilisateur) {
        if(!utilisateur.getEmail().contains("@")){
            throw new RuntimeException("email invalid");
        }
        if(!utilisateur.getEmail().contains(".")){
            throw new RuntimeException("email invalid");
        }
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findByEmail(utilisateur.getEmail());
        if(utilisateurOptional.isPresent()){
            throw new RuntimeException("L'utilisateur existe déja");
        }
        Role roleUtilisateur = new Role();
        roleUtilisateur.setLibelle(TypeDeRole.UTILISATEUR);
        utilisateur.setRole(roleUtilisateur);
        String hashedPassword = passwordEncoder.encode(utilisateur.getPassword());
        utilisateur.setPassword(hashedPassword);
        utilisateur = utilisateurRepository.save(utilisateur);
        this.validationService.enregistrer(utilisateur);
    }
}
