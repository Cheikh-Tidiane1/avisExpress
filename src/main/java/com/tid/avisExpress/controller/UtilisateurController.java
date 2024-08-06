package com.tid.avisExpress.controller;
import com.tid.avisExpress.model.Utilisateur;
import com.tid.avisExpress.services.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@Slf4j
public class UtilisateurController {

    private UtilisateurService utilisateurService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/inscription")
    public void inscription(@RequestBody Utilisateur utilisateur) {
        log.info("Inscription success ✅");
        utilisateurService.inscription(utilisateur);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/activation")
    public void activation(@RequestBody Map<String,String> activation) {
        log.info("Activation success ✅");
        utilisateurService.activation(activation);
    }
}
