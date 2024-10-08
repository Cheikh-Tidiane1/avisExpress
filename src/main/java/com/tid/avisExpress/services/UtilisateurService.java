package com.tid.avisExpress.services;
import com.tid.avisExpress.model.*;
import com.tid.avisExpress.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
@AllArgsConstructor
@Service
@Slf4j
public class UtilisateurService implements UserDetailsService {

    private UtilisateurRepository utilisateurRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private ValidationService validationService;
    private PasswordResetService passwordResetService;

    public void inscription(Utilisateur utilisateur) {
        if(!utilisateur.getEmail().contains("@")){
            throw new RuntimeException("email invalid");
        }
        if(!utilisateur.getEmail().contains(".")){
            throw new RuntimeException("email invalid");
        }
        Optional<Utilisateur> utilisateurOptional = this.utilisateurRepository.findByEmail(utilisateur.getEmail());
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

    public void activation (Map<String,String> activation){
        Validation validation = this.validationService.valideCode(activation.get("code"));
        if(Instant.now().isAfter(validation.getExpire())){
            throw new RuntimeException("Votre code a expiré");
        }
        Utilisateur utilisateurActif = this.utilisateurRepository.findById(validation.getUtilisateur().getId()).orElseThrow(()-> new RuntimeException("Utilisateur inconnu"));
        utilisateurActif.setActif(true);
        this.utilisateurRepository.save(utilisateurActif);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.utilisateurRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("Utilisateur non trouvé"));
    }

    public void modifierMdp(Map<String, String> users) {
        Utilisateur utilisateur = (Utilisateur) this.loadUserByUsername(users.get("email"));
        this.passwordResetService.saveNewPasswordReset(utilisateur);
    }

    public void nouveauMdp(Map<String, String> users) {
        Utilisateur utilisateur = (Utilisateur) this.loadUserByUsername(users.get("email"));
        PasswordReset passwordReset = this.passwordResetService.validCode(users.get("code"));
        if(passwordReset.getUtilisateur().getEmail().equals(utilisateur.getEmail())){
            String mdpCrypt = this.passwordEncoder.encode(users.get("password"));
            utilisateur.setPassword(mdpCrypt);
            this.utilisateurRepository.save(utilisateur);
        }
    }

    public Iterable<Utilisateur> listUser() {
        return this.utilisateurRepository.findAll();
    }
}
