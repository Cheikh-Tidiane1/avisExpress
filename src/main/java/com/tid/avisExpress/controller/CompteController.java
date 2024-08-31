package com.tid.avisExpress.controller;
import com.tid.avisExpress.dto.AuthenticationDto;
import com.tid.avisExpress.model.Utilisateur;
import com.tid.avisExpress.security.JwtService;
import com.tid.avisExpress.services.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@Slf4j
public class CompteController {

    private AuthenticationManager authenticationManager;
    private UtilisateurService utilisateurService;
    private JwtService jwtService ;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/inscription")
    public void inscription(@RequestBody Utilisateur utilisateur) {
        log.info("Inscription success ✅");
        this.utilisateurService.inscription(utilisateur);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/activation")
    public void activation(@RequestBody Map<String,String> activation) {
        log.info("Activation success ✅");
        utilisateurService.activation(activation);
    }


    @PostMapping(path = "/connexion")
    public Map<String,String> connexion(@RequestBody AuthenticationDto authenticationDto) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationDto.username(), authenticationDto.password())
        );
        if(authenticate.isAuthenticated()) {
            return this.jwtService.getJwtToken(authenticationDto.username());
        }
        return null ;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/deconnexion")
    public void deconnexion() {
        log.info("Déconnexion success ✅");
        this.jwtService.deconnexion();
    }


    @PostMapping(path = "/refreshToken")
    public Map<String,String> refreshToken(@RequestBody Map<String,String> refreshTokenRequest) {
       return  this.jwtService.refreshToken(refreshTokenRequest);
    }


    @PostMapping(path = "/modifierMdp")
    public void modifierMdp(@RequestBody Map<String,String> users) {
        this.utilisateurService.modifierMdp(users);
    }

    @PostMapping(path = "/nouveauMdp")
    public void nouveauMdp(@RequestBody Map<String,String> users) {
        this.utilisateurService.nouveauMdp(users);
    }
}
